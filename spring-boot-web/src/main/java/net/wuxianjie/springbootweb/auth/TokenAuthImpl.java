package net.wuxianjie.springbootweb.auth;

import cn.hutool.cache.impl.TimedCache;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.auth.dto.AuthData;
import net.wuxianjie.springbootweb.auth.dto.TokenPayload;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * 实现 Token 身份验证。
 *
 * @author 吴仙杰
 */
@Service
@RequiredArgsConstructor
public class TokenAuthImpl implements TokenAuth {

  private final TimedCache<String, AuthData> accessTokenCache;
  private final TokenService tokenService;

  @Override
  public AuthData authenticate(final String accessToken) {
    // 验证 JWT Token 本身（格式）是否合法
    final boolean legalToken = tokenService.isLegal(accessToken);
    if (!legalToken) {
      throw new IllegalArgumentException("Token 不合法");
    }

    // 解析 JWT Token 获取用户名及类型
    final TokenPayload payload = tokenService.parse(accessToken);

    // 判断 Token 是否为 Access Token
    if (!Objects.equals(payload.type(), AuthProps.TOKEN_TYPE_ACCESS)) {
      throw new IllegalArgumentException("API 鉴权请使用 Access Token");
    }

    // 通过用户名获取用户数据，并判断账号是否启用等信息
    final AuthData authData = Optional.ofNullable(accessTokenCache.get(payload.username()))
      .orElseThrow(() -> new RuntimeException("Token 已失效"));

    if (!Objects.equals(authData.accessToken(), accessToken)) {
      throw new RuntimeException("Token 已废弃");
    }

    if (authData.status() == AccountStatus.DISABLED) {
      throw new RuntimeException("账号已禁用");
    }

    return authData;
  }
}
