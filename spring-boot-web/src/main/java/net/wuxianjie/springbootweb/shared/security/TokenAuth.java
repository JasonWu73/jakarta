package net.wuxianjie.springbootweb.shared.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.List;

/**
 * 实现 Token 身份验证必须要实现的接口，详见 {@link TokenAuthFilter}。
 *
 * @author 吴仙杰
 **/
public interface TokenAuth {

  /**
   * 对 Access Token 执行身份验证，并返回该 Token 所对应的用户名。
   *
   * <p>需要实现的逻辑：
   *
   * <ol>
   *   <li>验证 Token 本身（格式）是否合法</li>
   *   <li>通过 Token 获取用户数据，并返回用户名</li>
   * </ol>
   *
   * @param accessToken 需要进行身份验证的 Access Token
   * @return 用户名 username
   * @throws Exception 当身份验证失败时抛出
   */
  String authenticate(String accessToken) throws Exception;

  /**
   * 向 Spring Security Context 中添加登录信息。
   *
   * @param user 登录后的用户数据
   * @param request {@link HttpServletRequest}
   */
  default void setSpringSecurityAuthenticatedContext(
    final UserDetails user,
    final HttpServletRequest request
  ) {
    final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
      user,
      null,
      user == null ? List.of() : user.getAuthorities()
    );

    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(token);
  }
}
