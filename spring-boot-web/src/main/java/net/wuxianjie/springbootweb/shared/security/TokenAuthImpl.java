package net.wuxianjie.springbootweb.shared.security;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.RequiredArgsConstructor;
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

  private final SecurityProps securityProps;
  private final TimedCache<String, AuthData> tokenCache;

  @Override
  public AuthData authenticate(final String accessToken) {
    // 验证 JWT Token 本身（格式）是否合法
    try {
      JWTValidator.of(accessToken)
        .validateAlgorithm(JWTSignerUtil.hs256(securityProps.getTokenKey().getBytes()))
        .validateDate();
    } catch (Exception e) {
      throw new RuntimeException("非法 Token");
    }

    // 解析 JWT Token 获取用户名及类型
    final JWT jwt = JWTUtil.parseToken(accessToken);

    final String username = Optional.ofNullable(jwt.getPayload(SecurityProps.JWT_PAYLOAD_USERNAME))
      .map(Object::toString)
      .orElseThrow(() -> new RuntimeException("错误 Token"));

    final String type = Optional.ofNullable(jwt.getPayload(SecurityProps.JWT_PAYLOAD_TYPE))
      .map(Object::toString)
      .orElseThrow(() -> new RuntimeException("错误 Token"));

    // 判断 Token 必须为 Access Token
    if (!Objects.equals(type, SecurityProps.TOKEN_TYPE_ACCESS)) {
      throw new RuntimeException("API 鉴权请使用 Access Token");
    }

    // 通过用户名获取用户数据
    final AuthData authData = Optional.ofNullable(tokenCache.get(username))
      .orElseThrow(() -> new RuntimeException("Token 已被失效"));

    if (!Objects.equals(authData.accessToken(), accessToken)) {
      throw new RuntimeException("Token 已被更新");
    }

    return authData;
  }
}
