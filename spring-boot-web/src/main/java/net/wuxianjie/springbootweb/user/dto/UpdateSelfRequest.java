package net.wuxianjie.springbootweb.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改本账号信息请求参数。
 *
 * @author 吴仙杰
 */
@Data
public class UpdateSelfRequest {

  /**
   * 昵称，必填。
   */
  @NotBlank(message = "昵称不能为空")
  private String nickname;

  /**
   * 旧密码。
   */
  private String oldPassword;

  /**
   * 新密码。
   */
  private String newPassword;
}
