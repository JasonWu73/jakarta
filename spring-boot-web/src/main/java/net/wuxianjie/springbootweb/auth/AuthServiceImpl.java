package net.wuxianjie.springbootweb.auth;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.auth.dto.AuthData;
import net.wuxianjie.springbootweb.auth.dto.GetTokenRequest;
import net.wuxianjie.springbootweb.auth.dto.TokenPayload;
import net.wuxianjie.springbootweb.auth.dto.TokenResponse;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import net.wuxianjie.springbootweb.shared.util.ServletUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 身份验证相关处理。
 *
 * @author 吴仙杰
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;
  private final TimedCache<String, AuthData> accessTokenCache;
  private final TokenAuth tokenAuth;
  private final AuthMapper authMapper;

  public ResponseEntity<TokenResponse> getToken(final GetTokenRequest req) {
    // 用户名不区分大小写
    req.setUsername(req.getUsername().toLowerCase());

    // 检索数据库，获取用户
    final AuthData auth = Optional.ofNullable(authMapper.selectByUsername(req.getUsername()))
      .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "用户名或密码错误"));

    // 检验账号是否已被禁用
    if (auth.getStatus() == AccountStatus.DISABLED) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "账号已禁用");
    }

    // 检验登录密码是否正确
    if (!passwordEncoder.matches(req.getPassword(), auth.getHashedPassword())) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "用户名或密码错误");
    }

    // 创建 Token
    final TokenData tokenData = createToken(req.getUsername());

    // 添加 Access Token 信息到登录缓存
    auth.setAccessToken(tokenData.accessToken);
    auth.setRefreshToken(tokenData.refreshToken);

    final TokenResponse resp = addLoginCache(auth);

    return ResponseEntity.ok(resp);
  }

  public ResponseEntity<TokenResponse> updateToken(final String refreshToken) {
    // 检验 JWT Token 本身（格式）是否合法
    final boolean legalToken = tokenService.isLegal(refreshToken);
    if (!legalToken) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Token 不合法");
    }

    // 解析 JWT Token 获取载荷
    final TokenPayload payload = tokenService.parse(refreshToken);

    // 检验 Token 类型是否为 Refresh Token
    if (!StrUtil.equals(payload.getType(), AuthProps.TOKEN_TYPE_REFRESH)) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "刷新请使用 Refresh Token");
    }

    // 检索登录缓存，获取用户
    final AuthData cachedAuth = Optional.ofNullable(accessTokenCache.get(payload.getUsername()))
      .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Token 已失效"));

    // 检验 Refresh Token 是否与登录缓存中的一致
    if (!StrUtil.equals(cachedAuth.getRefreshToken(), refreshToken)) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Token 已废弃");
    }

    // 检索数据库，获取新增的用户数据
    final AuthData auth = Optional.ofNullable(authMapper.selectByUsername(payload.getUsername()))
      .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "无效的 Token"));

    // 检验账号是否已被禁用
    if (auth.getStatus() == AccountStatus.DISABLED) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "账号已禁用");
    }

    // 创建 Token
    final TokenData tokenData = createToken(payload.getUsername());

    // 添加 Access Token 信息到登录缓存
    auth.setAccessToken(tokenData.accessToken);
    auth.setRefreshToken(tokenData.refreshToken);

    final TokenResponse resp = addLoginCache(auth);

    return ResponseEntity.ok(resp);
  }

  private TokenData createToken(final String username) {
    // 获取当前时间戳，精确到秒
    final long curTimeSec = System.currentTimeMillis() / 1000;

    // 创建 Access Token
    final String accessToken = tokenService.createToken(new TokenPayload(
      username,
      AuthProps.TOKEN_TYPE_ACCESS,
      AuthProps.TOKEN_ISSUER,
      curTimeSec + AuthProps.TOKEN_EXP_SEC
    ));

    // 创建 Refresh Token
    final String refreshToken = tokenService.createToken(new TokenPayload(
      username,
      AuthProps.TOKEN_TYPE_REFRESH,
      AuthProps.TOKEN_ISSUER,
      curTimeSec + AuthProps.TOKEN_EXP_SEC
    ));

    return new TokenData(accessToken, refreshToken);
  }

  private TokenResponse addLoginCache(final AuthData auth) {
    // 添加 Token 身份验证缓存
    accessTokenCache.put(auth.getUsername(), auth);

    // 将登录信息写入 Spring Security Context 中，以便记录登录日志进可获取用户名
    tokenAuth.setAuthenticatedContext(auth, ServletUtils.getCurrentRequest().orElseThrow());

    return new TokenResponse(
      auth.getAccessToken(),
      auth.getRefreshToken(),
      AuthProps.TOKEN_EXP_SEC,
      auth.getUsername(),
      auth.getNickname(),
      auth.getAuthorities()
    );
  }

  private record TokenData(String accessToken, String refreshToken) {}
}
