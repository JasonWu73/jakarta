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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 身份验证相关处理。
 *
 * @author 吴仙杰
 */
@Service
@RequiredArgsConstructor
public class AuthService {

  private final TokenService tokenService;
  private final TimedCache<String, AuthData> accessTokenCache;
  private final TokenAuth tokenAuth;

  /**
   * 获取 Access Token。
   *
   * @param request 请求参数
   * @return Access Token 相关信息
   */
  public TokenResponse getToken(final GetTokenRequest request) {
    // 执行身份验证
    if (!Objects.equals(request.getUsername(), "wxj")) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "用户或密码错误");
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
    final AuthData authData = new AuthData(
      100,
      request.getUsername(),
      "测试用户",
      false,
      List.of(),
      accessToken,
      refreshToken
    );

    accessTokenCache.put(request.getUsername(), authData);

    // 添加 Spring Security Context 以便记录登录日志进可获取用户名
    tokenAuth.setAuthenticatedContext(authData, ServletUtils.getCurrentRequest().orElseThrow());

    return new TokenResponse(
      accessToken,
      refreshToken,
      AuthProps.TOKEN_EXPIRATION_SEC,
      request.getUsername(),
      "测试用户",
      List.of()
    );
  }
}
