package net.wuxianjie.springbootweb.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通过身份验证后的 Token 响应数据。
 *
 * @author 吴仙杰
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

  /**
   * 用于接口访问的 Access Token。
   */
  private String accessToken;
  /**
   * 用于刷新的 Refresh Token。
   */
  private String refreshToken;
  /**
   * Token 多少秒后过期。
   */
  private int expiresInSec;
  /**
   * 用户名。
   */
  private String username;
  /**
   * 昵称。
   */
  private String nickname;
  /**
   * 功能权限列表。
   */
  private List<String> authorities;
}
