package net.wuxianjie.springbootweb.role;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色数据库表实体类。
 *
 * @author 吴仙杰
 */
@Data
public class Role {

  /**
   * 角色 id。
   */
  private Long id;

  /**
   * 角色名。
   */
  private String name;

  /**
   * 以英文逗号分隔的功能权限字符串。
   */
  private String authorities;

  /**
   * 父角色 id。
   */
  private Long parentId;

  /**
   * 父角色名。
   */
  private String parentName;

  /**
   * 节点全路径，以 {@code .} 分隔的角色 id。
   */
  private String fullPath;

  /**
   * 创建时间。
   */
  private LocalDateTime createdAt;

  /**
   * 更新时间。
   */
  private LocalDateTime updatedAt;

  /**
   * 备注。
   */
  private String remark;
}
