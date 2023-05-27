package net.wuxianjie.springbootweb.role.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 更新角色请求参数。
 *
 * @author 吴仙杰
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleRequest {

  /**
   * 角色名，必填。
   */
  @NotBlank(message = "角色名不能为空")
  private String name;
  /**
   * 功能权限列表。
   */
  private List<String> authorities;
  /**
   * 父角色 id，必填。
   */
  @NotNull(message = "父角色 id 不能为 null")
  private Long parentId;
  /**
   * 备注。
   */
  private String remark;
}
