package net.wuxianjie.springbootweb.auth.dto;

import cn.hutool.jwt.JWTPayload;

/**
 * JWT Token 的载荷。
 *
 * @param iss Token 签发者
 * @param username 登录用户名
 * @param type Token 类型
 * @param exp JWT 的过期时间，单位：秒。参考 {@link JWTPayload#EXPIRES_AT}
 */
public record TokenPayload(
  String username,
  String type,
  String iss,
  long exp
) {
}
