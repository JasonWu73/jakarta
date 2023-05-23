package net.wuxianjie.springbootweb.user.dto;

import net.wuxianjie.springbootweb.auth.AccountStatus;

import java.time.LocalDateTime;

/**
 * 用户详情数据。
 *
 * @param id 用户 id
 * @param username 用户名
 * @param nickname 昵称
 * @param status 账号状态，0：禁用，1：启用
 * @param roleId 角色 id
 * @param roleName 角色名
 * @param fullPath 角色路径
 * @param updatedAt 更新时间
 * @param remark 备注
 * @author 吴仙杰
 */
public record UserDetailResponse(
  long id,
  String username,
  String nickname,
  AccountStatus status,
  long roleId,
  String roleName,
  String fullPath,
  LocalDateTime updatedAt,
  String remark
) {
}
