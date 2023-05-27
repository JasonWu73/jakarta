package net.wuxianjie.springbootweb.auth;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wuxianjie.springbootweb.auth.dto.TokenPayload;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * JWT Token 的处理逻辑。
 *
 * @author 吴仙杰
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

  private final AuthProps authProps;

  /**
   * 判断 Token 是否合法。
   *
   * @param token 需要判断的 Token
   * @return Token 是否合法
   */
  public boolean isLegal(final String token) {
    try {
      JWTValidator.of(token)
        .validateAlgorithm(JWTSignerUtil.hs256(authProps.getTokenKey().getBytes()))
        .validateDate();
    } catch (Exception e) {
      log.warn("Token 不合法 [token={};error={}]", token, e.getMessage());

      return false;
    }

    return true;
  }

  /**
   * 创建 JWT Token。
   *
   * @param payload 写入 Token 的载荷
   * @return JWT Token
   */
  public String createToken(final TokenPayload payload) {
    return JWTUtil.createToken(
      Map.ofEntries(
        Map.entry(AuthProps.JWT_PAYLOAD_USERNAME, payload.getUsername()),
        Map.entry(AuthProps.JWT_PAYLOAD_TYPE, payload.getType()),
        Map.entry(JWTPayload.ISSUER, payload.getIss()),
        Map.entry(JWTPayload.EXPIRES_AT, payload.getExp())
      ),
      authProps.getTokenKey().getBytes()
    );
  }

  /**
   * 解析 JWT Token，并获取载荷。
   *
   * @param token 需要解析的 Token
   * @return Token 载荷
   */
  public TokenPayload parse(final String token) {
    final JWT jwt = JWTUtil.parseToken(token);

    final String username = Optional.ofNullable(jwt.getPayload(AuthProps.JWT_PAYLOAD_USERNAME))
      .map(Object::toString)
      .orElseThrow(() -> {
        log.warn("JWT 缺少必要载荷 [{}]", AuthProps.JWT_PAYLOAD_USERNAME);

        return new IllegalArgumentException("错误的 Token");
      });

    final String type = Optional.ofNullable(jwt.getPayload(AuthProps.JWT_PAYLOAD_TYPE))
      .map(Object::toString)
      .orElseThrow(() -> {
        log.warn("JWT 缺少必要载荷 [{}]", AuthProps.JWT_PAYLOAD_TYPE);

        return new IllegalArgumentException("错误的 Token");
      });

    final String iss = Optional.ofNullable(jwt.getPayload(JWTPayload.ISSUER))
      .map(Object::toString)
      .orElseThrow(() -> {
        log.warn("JWT 缺少必要载荷 [{}]", JWTPayload.ISSUER);

        return new IllegalArgumentException("错误的 Token");
      });

    final long exp = Optional.ofNullable(jwt.getPayload(JWTPayload.EXPIRES_AT))
      .map(o -> Long.parseLong(o.toString()))
      .orElseThrow(() -> {
        log.warn("JWT 缺少必要载荷 [{}]", JWTPayload.EXPIRES_AT);

        return new IllegalArgumentException("错误的 Token");
      });

    return new TokenPayload(username, type, iss, exp);
  }
}
