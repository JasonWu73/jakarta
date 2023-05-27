package net.wuxianjie.springbootweb.role.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 新增角色请求参数。
 *
 * @author 吴仙杰
 */
@Data
public class AddRoleRequest {

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
   * 父角色 id。
   */
  private Long parentId;
  /**
   * 备注。
   */
  private String remark;
}
