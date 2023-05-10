package net.wuxianjie.springbootweb.auth;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.auth.dto.*;
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
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;
  private final TimedCache<String, AuthData> accessTokenCache;
  private final TokenAuth tokenAuth;
  private final AuthMapper authMapper;

  /**
   * 获取 Access Token。
   *
   * @param request 请求参数
   * @return Access Token 相关信息
   */
  public ResponseEntity<TokenResponse> getToken(final GetTokenRequest request) {
    // 通过用户名查询数据库获取用户数据
    final RawAuthData rawAuth = Optional.ofNullable(
      authMapper.selectByUsername(request.getUsername())
    ).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "用户名或密码错误"));

    // 判断密码是否正确
    if (!passwordEncoder.matches(request.getPassword(), rawAuth.hashedPassword())) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "用户名或密码错误");
    }

    // 创建 Token
    final long timestampSec = System.currentTimeMillis() / 1000;
    final String accessToken = tokenService.createToken(new TokenPayload(
      request.getUsername(),
      AuthProps.TOKEN_TYPE_ACCESS,
      AuthProps.TOKEN_ISSUER,
      timestampSec + AuthProps.TOKEN_EXPIRATION_SEC
    ));

    final String refreshToken = tokenService.createToken(new TokenPayload(
      request.getUsername(),
      AuthProps.TOKEN_TYPE_REFRESH,
      AuthProps.TOKEN_ISSUER,
      timestampSec + AuthProps.TOKEN_EXPIRATION_SEC
    ));

    // 添加 Token 缓存
    final AuthData auth = new AuthData(
      rawAuth.userId(),
      rawAuth.username(),
      rawAuth.nickname(),
      rawAuth.status(),
      StrUtil.split(rawAuth.menus(), StrUtil.COMMA, true, true),
      accessToken,
      refreshToken
    );
    accessTokenCache.put(request.getUsername(), auth);

    // 添加 Spring Security Context 以便记录登录日志进可获取用户名
    tokenAuth.setAuthenticatedContext(auth, ServletUtils.getCurrentRequest().orElseThrow());

    return ResponseEntity.ok(new TokenResponse(
      accessToken,
      refreshToken,
      AuthProps.TOKEN_EXPIRATION_SEC,
      request.getUsername(),
      auth.nickname(),
      auth.authorities()
    ));
  }

  public ResponseEntity<TokenResponse> updateToken(final String refreshToken) {
    // 验证 JWT Token 本身（格式）是否合法
    final boolean legalToken = tokenService.isLegal(refreshToken);
    if (!legalToken) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Token 不合法");
    }

    // 解析 JWT Token 获取用户名及类型
    final TokenPayload payload = tokenService.parse(refreshToken);

    // 判断 Token 是否为 Refresh Token
    if (!Objects.equals(payload.type(), AuthProps.TOKEN_TYPE_REFRESH)) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "刷新请使用 Refresh Token");
    }

    // 通过用户名获取登录缓存中的用户数据，并判断 Token 是否正确
    final AuthData authData = Optional.ofNullable(accessTokenCache.get(payload.username()))
      .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Token 已失效"));

    if (!Objects.equals(authData.refreshToken(), refreshToken)) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Token 已废弃");
    }

    // 查询数据库获取用户数据
    final RawAuthData rawAuth = Optional.ofNullable(
      authMapper.selectByUsername(payload.username())
    ).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "无效的 Token"));

    // 创建 Token
    final long timestampSec = System.currentTimeMillis() / 1000;
    final String accessToken = tokenService.createToken(new TokenPayload(
      payload.username(),
      AuthProps.TOKEN_TYPE_ACCESS,
      AuthProps.TOKEN_ISSUER,
      timestampSec + AuthProps.TOKEN_EXPIRATION_SEC
    ));

    final String newRefreshToken = tokenService.createToken(new TokenPayload(
      payload.username(),
      AuthProps.TOKEN_TYPE_REFRESH,
      AuthProps.TOKEN_ISSUER,
      timestampSec + AuthProps.TOKEN_EXPIRATION_SEC
    ));

    // 添加 Token 缓存
    final AuthData auth = new AuthData(
      rawAuth.userId(),
      rawAuth.username(),
      rawAuth.nickname(),
      rawAuth.status(),
      StrUtil.split(rawAuth.menus(), StrUtil.COMMA, true, true),
      accessToken,
      newRefreshToken
    );
    accessTokenCache.put(payload.username(), auth);

    return ResponseEntity.ok(new TokenResponse(
      accessToken,
      newRefreshToken,
      AuthProps.TOKEN_EXPIRATION_SEC,
      payload.username(),
      auth.nickname(),
      auth.authorities()
    ));
  }
}
