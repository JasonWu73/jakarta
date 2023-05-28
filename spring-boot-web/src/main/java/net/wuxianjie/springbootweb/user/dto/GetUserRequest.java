package net.wuxianjie.springbootweb.user.dto;

import lombok.Data;
import net.wuxianjie.springbootweb.auth.AccountStatus;
import net.wuxianjie.springbootweb.shared.validation.EnumValidator;

/**
 * 获取用户请求参数。
 *
 * @author 吴仙杰
 */
@Data
public class GetUserRequest {

  /**
   * 用户名。
   */
  private String username;
  /**
   * 昵称。
   */
  private String nickname;
  /**
   * 账号状态。
   */
  @EnumValidator(value = AccountStatus.class, message = "账号状态不合法")
  private Integer status;
  /**
   * 角色名。
   */
  private String roleName;
}
