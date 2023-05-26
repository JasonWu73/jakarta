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

  private final TokenService tokenService;
  private final TimedCache<String, AuthData> accessTokenCache;

  @Override
  public AuthData authenticate(final String accessToken) {
    // 验证 JWT Token 本身（格式）是否合法
    final boolean legalToken = tokenService.isLegal(accessToken);

    if (!legalToken) {
      throw new IllegalArgumentException("Token 不合法");
    }

    // 解析 JWT Token 获取载荷
    final TokenPayload payload = tokenService.parse(accessToken);

    // 校验 Token 是否为 Access Token
    if (!Objects.equals(payload.getType(), AuthProps.TOKEN_TYPE_ACCESS)) {
      throw new IllegalArgumentException("API 鉴权请使用 Access Token");
    }

    // 检索登录缓存获取用户数据
    final AuthData cachedAuth = Optional.ofNullable(accessTokenCache.get(payload.getUsername()))
      .orElseThrow(() -> new RuntimeException("Token 已失效"));

    // 校验 Access Token 是否匹配
    if (!Objects.equals(cachedAuth.getAccessToken(), accessToken)) {
      throw new RuntimeException("Token 已废弃");
    }

    // 验证账号是否可用
    if (cachedAuth.getStatus() == AccountStatus.DISABLED) {
      throw new RuntimeException("账号已禁用");
    }

    return cachedAuth;
  }
}
