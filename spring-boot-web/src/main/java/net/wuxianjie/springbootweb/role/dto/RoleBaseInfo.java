package net.wuxianjie.springbootweb.role.dto;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色的基础数据。
 *
 * @author 吴仙杰
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleBaseInfo {

  /**
   * 角色 id。
   */
  private long id;
  /**
   * 角色名。
   */
  private String name;
  /**
   * 功能权限列表。
   */
  private List<String> authorities;
  /**
   * 父角色 id。
   */
  private Long parentId;
  /**
   * 父角色名。
   */
  private String parentName;
  /**
   * 角色全路径。
   */
  private String fullPath;
  /**
   * 更新时间。
   */
  private LocalDateTime updatedAt;
  /**
   * 备注。
   */
  private String remark;

  // 用于 MyBatis 的 ResultMap
  public RoleBaseInfo(
    final long id,
    final String name,
    final String authorities,
    final Long parentId,
    final String parentName,
    final String fullPath,
    final LocalDateTime updatedAt,
    final String remark
  ) {
    this.id = id;
    this.name = name;
    this.parentId = parentId;
    this.parentName = parentName;
    this.fullPath = fullPath;
    this.updatedAt = updatedAt;
    this.remark = remark;

    this.authorities = StrUtil.split(authorities, StrUtil.COMMA, true, true);
  }
}
