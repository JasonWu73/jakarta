package net.wuxianjie.springbootweb.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import net.wuxianjie.springbootweb.auth.AccountStatus;
import net.wuxianjie.springbootweb.shared.validation.EnumValidator;

/**
 * 获取用户请求参数。
 *
 * @author 吴仙杰
 */
@Data
public class AddUserRequest {

  /**
   * 用户名，只能包含中文、数字或 _、且必须以中文或英文开头，必填。
   */
  @Pattern(
    regexp = "(^\\s*$|^[\\u4E00-\\u9FA5A-Za-z][\\u4E00-\\u9FA5A-Za-z\\d_]+$)",
    message = "用户名只能包含中文, 数字或_, 且必须以中文或英文开头"
  )
  @NotBlank(message = "用户名不能为空")
  private String username;

  /**
   * 昵称，必填。
   */
  @NotBlank(message = "昵称不能为空")
  private String nickname;

  /**
   * 密码，必填。
   */
  @NotBlank(message = "密码不能为空")
  private String password;

  /**
   * 账号状态，0：禁用，1：启用，必填。
   */
  @EnumValidator(value = AccountStatus.class, message = "账号状态不合法")
  @NotNull(message = "账号状态不能为 null")
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
