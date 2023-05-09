package net.wuxianjie.springbootweb.auth.dto;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.List;

/**
 * 通过身份验证后的 Token 详细数据。
 *
 * @param userId 用户 id
 * @param username 用户名
 * @param nickname 用户昵称
 * @param enabled 是否启用
 * @param authorities 权限列表
 * @param accessToken Access Token
 * @param refreshToken Refresh Token
 */
public record AuthData(
  long userId,
  String username,
  String nickname,
  boolean enabled,
  List<String> authorities,
  String accessToken,
  String refreshToken
) {

  /**
   * 获取 Spring Security 所需要的权限集合。
   *
   * @return Spring Security 权限
   */
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.createAuthorityList(
      ArrayUtil.toArray(authorities, String.class)
    );
  }
}
