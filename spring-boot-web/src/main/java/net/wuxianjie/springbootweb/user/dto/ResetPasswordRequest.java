package net.wuxianjie.springbootweb.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 重置密码请求参数。
 *
 * @author 吴仙杰
 */
@Data
public class ResetPasswordRequest {

  /**
   * 新密码，必填。
   */
  @NotBlank(message = "新密码不能为空")
  private String password;
}
