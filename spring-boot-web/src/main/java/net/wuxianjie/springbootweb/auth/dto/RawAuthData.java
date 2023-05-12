package net.wuxianjie.springbootweb.auth.dto;

import net.wuxianjie.springbootweb.auth.AccountStatus;

/**
 * Token 身份验证后的原始数据。
 *
 * @param userId 用户 id
 * @param username 用户名
 * @param nickname 用户昵称
 * @param hashedPassword Hash 计算后的密码
 * @param status 账号状态
 * @param roleId 角色 id
 * @param authorities 以英文逗号分隔的功能权限字符串
 */
public record RawAuthData(
  long userId,
  String username,
  String nickname,
  String hashedPassword,
  AccountStatus status,
  long roleId,
  String authorities
) {
}
