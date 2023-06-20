package net.wuxianjie.springbootweb.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 获取 Access Token 请求参数。
 *
 * @author 吴仙杰
 */
@Data
public class GetTokenRequest {

  /**
   * 用户名（不区分大小写），必填。
   */
  @NotBlank(message = "用户名不能为空")
  private String username;
  /**
   * 密码，必填。
   */
  @NotBlank(message = "密码不能为空")
  private String password;
}
