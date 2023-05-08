package net.wuxianjie.springbootweb.shared.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import net.wuxianjie.springbootweb.shared.security.AuthData;
import net.wuxianjie.springbootweb.shared.security.AuthUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * Servlet 相关工具类。
 *
 * @author 吴仙杰
 */
public class ServletUtils {

  /**
   * 获取与当前线程绑定的 HTTP Servlet 请求对象。
   *
   * @return HTTP Servlet 请求对象
   */
  public static Optional<HttpServletRequest> getCurrentRequest() {
    return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
      .map(attributes -> {
        if (attributes instanceof ServletRequestAttributes attr) {
          return attr.getRequest();
        }

        return null;
      });
  }

  /**
   * 获取包含客户端信息的字符串。
   *
   * @return {@code api=[{} {}];client={};user={}}
   */
  public static String getClientInfo() {
    final HttpServletRequest request = ServletUtils.getCurrentRequest().orElseThrow();

    final String username = AuthUtils.getCurrentUser()
      .map(AuthData::username)
      .orElse(null);

    return StrUtil.format(
      "api=[{} {}];client={};user={}",
      request.getMethod(),
      request.getRequestURI(),
      JakartaServletUtil.getClientIP(request),
      username
    );
  }
}
