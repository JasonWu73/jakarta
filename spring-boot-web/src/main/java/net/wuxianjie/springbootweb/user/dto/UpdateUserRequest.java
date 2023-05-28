package net.wuxianjie.springbootweb.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.wuxianjie.springbootweb.auth.AccountStatus;
import net.wuxianjie.springbootweb.shared.validation.EnumValidator;

/**
 * 更新用户请求参数。
 *
 * @author 吴仙杰
 */
@Data
public class UpdateUserRequest {

  /**
   * 昵称，必填。
   */
  @NotBlank(message = "昵称不能为空")
  private String nickname;
  /**
   * 账号状态，必填。
   */
  @NotNull(message = "账号状态不能为 null")
  @EnumValidator(value = AccountStatus.class, message = "账号状态不合法")
  private Integer status;
  /**
   * 角色 id，必填。
   */
  @NotNull(message = "角色 id 不能为 null")
  private Long roleId;
  /**
   * 备注。
   */
  private String remark;
}
