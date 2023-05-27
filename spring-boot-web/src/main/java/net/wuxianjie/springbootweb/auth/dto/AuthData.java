package net.wuxianjie.springbootweb.auth.dto;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.wuxianjie.springbootweb.auth.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.List;

/**
 * 通过身份验证后的详细数据。
 *
 * @author 吴仙杰
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthData {

  /**
   * 用户 id。
   */
  private long userId;
  /**
   * 用户名。
   */
  private String username;
  /**
   * 哈希密码。
   */
  private String hashedPassword;
  /**
   * 昵称。
   */
  private String nickname;
  /**
   * 账号状态。
   */
  private AccountStatus status;
  /**
   * 功能权限列表。
   */
  private List<String> authorities;
  /**
   * 用于接口访问的 Access Token。
   */
  private String accessToken;
  /**
   * 用于刷新的 Refresh Token。
   */
  private String refreshToken;

  // 用于 MyBatis 的 ResultMap
  public AuthData(
    final long userId,
    final String username,
    final String hashedPassword,
    final String nickname,
    final AccountStatus status,
    final String authorities
  ) {
    this.userId = userId;
    this.username = username;
    this.hashedPassword = hashedPassword;
    this.nickname = nickname;
    this.status = status;

    this.authorities = StrUtil.split(authorities, StrUtil.COMMA, true, true);
  }

  // 用于 Spring Security 的权限集合
  public Collection<? extends GrantedAuthority> getSpringSecurityAuthorities() {
    return AuthorityUtils.createAuthorityList(ArrayUtil.toArray(authorities, String.class));
  }
}
