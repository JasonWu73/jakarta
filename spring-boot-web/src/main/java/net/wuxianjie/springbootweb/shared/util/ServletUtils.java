package net.wuxianjie.springbootweb.shared.util;

import jakarta.servlet.http.HttpServletRequest;
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
}
