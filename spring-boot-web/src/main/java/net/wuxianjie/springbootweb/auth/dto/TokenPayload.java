package net.wuxianjie.springbootweb.auth.dto;

import cn.hutool.jwt.JWTPayload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT Token 的载荷。
 *
 * @author 吴仙杰
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenPayload {

  /**
   * 用户名。
   */
  private String username;
  /**
   * Token 类型，详见 {@link net.wuxianjie.springbootweb.auth.AuthProps#JWT_PAYLOAD_TYPE}。
   */
  private String type;

  /**
   * JWT Token 的签发者，参考 {@link JWTPayload#ISSUER}。
   */
  private String iss;
  /**
   * JWT Token 的过期时间，单位：秒。参考 {@link JWTPayload#EXPIRES_AT}
   */
  private long exp;
}
