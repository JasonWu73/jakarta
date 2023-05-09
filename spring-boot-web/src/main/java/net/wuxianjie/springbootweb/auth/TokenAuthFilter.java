package net.wuxianjie.springbootweb.auth;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
 * 自定义 Spring Security Token 认证过滤器。
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
   * 携带 Token 授权信息的请求头值前缀：{@code Authorization: Bearer {{accessToken}}}。
   */
  public static final String BEARER_PREFIX = "Bearer ";

  private final HandlerExceptionResolver handlerExceptionResolver;
  private final TokenAuth tokenAuth;

  @Override
  protected void doFilterInternal(
    final HttpServletRequest request,
    final HttpServletResponse response,
    final FilterChain filterChain
  ) throws ServletException, IOException {
    // 从请求头中获取 Access Token
    final Optional<String> bearerTokenOpt = Optional.ofNullable(
      JakartaServletUtil.getHeaderIgnoreCase(request, HttpHeaders.AUTHORIZATION)
    );

    if (bearerTokenOpt.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    if (!StrUtil.startWith(bearerTokenOpt.get(), BEARER_PREFIX)) {
      resolveError(request, response, "授权信息格式有误");
      return;
    }

    final String accessToken = StrUtil.removePrefix(bearerTokenOpt.get(), BEARER_PREFIX);

    // 执行 Access Token 身份验证，并获取用户数据
    final AuthData authData;
    try {
      authData = tokenAuth.authenticate(accessToken);
    } catch (Exception e) {
      resolveError(request, response, e.getMessage());
      return;
    }

    // 将登录信息写入 Spring Security Context 中
    tokenAuth.setAuthenticatedContext(authData, request);

    // 继续执行下一个过滤器
    filterChain.doFilter(request, response);
  }

  private void resolveError(
    final HttpServletRequest request,
    final HttpServletResponse response,
    final String error
  ) {
    handlerExceptionResolver.resolveException(
      request,
      response,
      null,
      new ApiException(HttpStatus.UNAUTHORIZED, error)
    );
  }
}
