package net.wuxianjie.springbootweb.auth.dto;

/**
 * 通过身份验证后的 Token 详细数据。
 *
 * @param accessToken 用于接口访问的 Access Token
 * @param refreshToken 用于刷新的 Refresh Token
 * @param expiresInSec Token 多少秒后过期
 * @param username 用户名
 * @param nickname 昵称
 * @param authorities 以英文逗号分隔的功能菜单（权限）字符串
 */
public record TokenResponse(
  String accessToken,
  String refreshToken,
  int expiresInSec,
  String username,
  String nickname,
  String authorities
) {
}
