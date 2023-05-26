package net.wuxianjie.springbootweb.auth;

import cn.hutool.cache.impl.TimedCache;
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

import java.util.Objects;
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
    // 检索数据库获取用户数据
    final String username = req.getUsername();

    final AuthData auth = Optional.ofNullable(authMapper.selectByUsername(username))
      .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "用户名或密码错误"));

    // 校验登录密码是否正确
    if (!passwordEncoder.matches(req.getPassword(), auth.getHashedPassword())) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "用户名或密码错误");
    }

    // 创建 Token
    final TokenData tokenData = createToken(username);

    // 添加登录缓存
    auth.setAccessToken(tokenData.accessToken);
    auth.setRefreshToken(tokenData.refreshToken);

    return ResponseEntity.ok(addLoginCache(auth));
  }

  public ResponseEntity<TokenResponse> updateToken(final String refreshToken) {
    // 验证 JWT Token 本身（格式）是否合法
    final boolean legalToken = tokenService.isLegal(refreshToken);
    if (!legalToken) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Token 不合法");
    }

    // 解析 JWT Token 获取载荷
    final TokenPayload payload = tokenService.parse(refreshToken);

    // 校验 Token 是否为 Refresh Token
    if (!Objects.equals(payload.getType(), AuthProps.TOKEN_TYPE_REFRESH)) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "刷新请使用 Refresh Token");
    }

    // 检索登录缓存获取用户数据
    final String username = payload.getUsername();

    final AuthData cachedAuth = Optional.ofNullable(accessTokenCache.get(username))
      .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Token 已失效"));

    // 校验 Refresh Token 是否匹配
    if (!Objects.equals(cachedAuth.getRefreshToken(), refreshToken)) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Token 已废弃");
    }

    // 检索数据库获取用户数据
    final AuthData auth = Optional.ofNullable(authMapper.selectByUsername(username))
      .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "无效的 Token"));

    // 创建 Token
    final TokenData tokenData = createToken(username);

    // 添加登录缓存
    auth.setAccessToken(tokenData.accessToken);
    auth.setRefreshToken(tokenData.refreshToken);

    return ResponseEntity.ok(addLoginCache(auth));
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
    // 添加 Token 身份认证缓存
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
