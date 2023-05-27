package net.wuxianjie.springbootweb.role.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 角色列表项响应数据。
 *
 * @author 吴仙杰
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleItemResponse {

  /**
   * 角色 id。
   */
  private long id;
  /**
   * 角色名。
   */
  private String name;
  /**
   * 父角色 id。
   */
  private Long parentId;
  /**
   * 父角色名。
   */
  private String parentName;
  /**
   * 更新时间。
   */
  private LocalDateTime updatedAt;
}
