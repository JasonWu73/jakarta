package net.wuxianjie.springbootweb.auth;

import jakarta.servlet.http.HttpServletRequest;
import net.wuxianjie.springbootweb.auth.dto.AuthData;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.ArrayList;

/**
 * Token 身份验证接口，用于 {@link TokenAuthFilter}。
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
   *   <li>验证 JWT Token 本身（格式）是否合法</li>
   *   <li>解析 JWT Token 获取用户名及类型</li>
   *   <li>Token 类型必须为 {@link AuthProps#TOKEN_TYPE_ACCESS}</li>
   *   <li>通过用户名获取用户数据，并判断账号是否启用等信息</li>
   * </ol>
   *
   * @param accessToken 需要进行身份验证的 Access Token
   * @return 身份验证通过后的用户信息
   * @throws Exception 当身份验证失败时抛出
   */
  AuthData authenticate(String accessToken) throws Exception;

  /**
   * 将登录信息写入 Spring Security Context 以便后续其他代码可从上下文中获取登录数据。
   *
   * @param auth 登录成功后的数据
   * @param req 当前 HTTP 请求
   */
  default void setAuthenticatedContext(final AuthData auth, final HttpServletRequest req) {
    final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
      auth,
      null,
      auth == null ? new ArrayList<>() : auth.getSpringSecurityAuthorities()
    );

    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

    SecurityContextHolder.getContext().setAuthentication(token);
  }
}
