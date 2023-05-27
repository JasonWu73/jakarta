package net.wuxianjie.springbootweb.auth;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.auth.dto.AuthData;
import net.wuxianjie.springbootweb.auth.dto.TokenPayload;
import org.springframework.stereotype.Service;

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

    // 判断 Token 类型是否为 Access Token
    if (!StrUtil.equals(payload.getType(), AuthProps.TOKEN_TYPE_ACCESS)) {
      throw new IllegalArgumentException("API 鉴权请使用 Access Token");
    }

    // 从登录缓存中获取用户数据
    final AuthData cachedAuth = Optional.ofNullable(accessTokenCache.get(payload.getUsername()))
      .orElseThrow(() -> new RuntimeException("Token 已失效"));

    // 判断 Access Token 是否与登录缓存中的一致
    if (!StrUtil.equals(cachedAuth.getAccessToken(), accessToken)) {
      throw new RuntimeException("Token 已废弃");
    }

    // 判断账号是否已被禁用
    if (cachedAuth.getStatus() == AccountStatus.DISABLED) {
      throw new RuntimeException("账号已禁用");
    }

    return cachedAuth;
  }
}
