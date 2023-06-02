package net.wuxianjie.springbootweb.test.securitymvc;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JpaUserService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    // 检索数据库，获取账号信息
    final User user = Optional.ofNullable(userRepository.findByUsername(username))
      .orElseThrow(() -> new UsernameNotFoundException("用户名不存在"));

    // 构造 Spring Security 登录用户数据
    return new org.springframework.security.core.userdetails.User(
      user.getUsername(),
      user.getHashedPassword(),
      toAuthorityList(user.getRole().getAuthorities())
    );
  }

  private Collection<? extends GrantedAuthority> toAuthorityList(final String authorities) {
    return AuthorityUtils.createAuthorityList(StrUtil.splitToArray(authorities, StrUtil.COMMA));
  }
}
