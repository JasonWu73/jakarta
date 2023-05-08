package net.wuxianjie.springbootweb.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 自定义 Spring Security Token 认证过滤器。
 *
 * <ul>
 *   <li>实现 Spring Security 的 {@link UserDetails} 接口，定义登录用户数据</li>
 *   <li>实现 Spring Security 的 {@link UserDetailsService} 接口，定义通过“用户名”获取登录用户数据的业务方法</li>
 *   <li>实现自定义的 {@link TokenAuth} 接口，定义对 Access Token 的身份验证，即登录逻辑</li>
 * </ul>
 *
 * @author 吴仙杰
 */
@Component
@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
    final HttpServletRequest request,
    final HttpServletResponse response,
    final FilterChain filterChain
  ) throws ServletException, IOException {

  }
}
