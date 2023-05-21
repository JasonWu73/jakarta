package net.wuxianjie.springbootweb.role.dto;

import java.time.LocalDateTime;

/**
 * 角色列表数据。
 *
 * @param id 角色 id
 * @param name 角色名
 * @param authorities 以英文逗号分隔的功能权限字符串
 * @param parentId 父角色 id
 * @param parentName 父角色名
 * @param updatedAt 更新时间
 * @param remark 备注
 */
public record RoleResponse(
  long id,
  String name,
  String authorities,
  Long parentId,
  String parentName,
  LocalDateTime updatedAt,
  String remark
) {
}
