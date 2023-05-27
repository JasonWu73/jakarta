package net.wuxianjie.springbootweb.auth;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.auth.dto.AuthData;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Optional;

/**
 * 自定义 Spring Security Token 身份验证过滤器。
 *
 * <ul>
 *   <li>实现自定义的 {@link TokenAuth} 接口，定义对 Access Token 的身份验证，即登录逻辑</li>
 * </ul>
 *
 * @author 吴仙杰
 */
@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {

  /**
   * 携带 Token 授权信息的请求头值前缀：
   *
   * <pre>{@code
   *   "Authorization: Bearer {{accessToken}}"
   * }</pre>
   */
  public static final String BEARER_PREFIX = "Bearer ";

  private final HandlerExceptionResolver handlerExceptionResolver;
  private final TokenAuth tokenAuth;

  @Override
  protected void doFilterInternal(
    @NonNull final HttpServletRequest req,
    @NonNull final HttpServletResponse resp,
    @NonNull final FilterChain chain
  ) throws ServletException, IOException {
    // 从请求头中获取 Access Token
    final Optional<String> bearerOpt = Optional.ofNullable(
      JakartaServletUtil.getHeaderIgnoreCase(req, HttpHeaders.AUTHORIZATION)
    );

    if (bearerOpt.isEmpty()) {
      chain.doFilter(req, resp);

      return;
    }

    if (!StrUtil.startWith(bearerOpt.get(), BEARER_PREFIX)) {
      resolveUnauthorizedError(req, resp, "授权信息格式有误");

      return;
    }

    final String token = StrUtil.removePrefix(bearerOpt.get(), BEARER_PREFIX);

    // 执行 Access Token 身份验证，并获取用户数据
    final AuthData auth;

    try {
      auth = tokenAuth.authenticate(token);
    } catch (Exception e) {
      resolveUnauthorizedError(req, resp, e.getMessage());

      return;
    }

    // 将登录信息写入 Spring Security Context 中
    tokenAuth.setAuthenticatedContext(auth, req);

    // 继续执行下一个过滤器
    chain.doFilter(req, resp);
  }

  private void resolveUnauthorizedError(final HttpServletRequest req, final HttpServletResponse resp, final String err) {
    handlerExceptionResolver.resolveException(req, resp, null, new ApiException(HttpStatus.UNAUTHORIZED, err));
  }
}
