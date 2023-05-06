package net.wuxianjie.springbootweb.shared.spa;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import net.wuxianjie.springbootweb.shared.restapi.ErrorResponse;
import net.wuxianjie.springbootweb.shared.util.ServletUtils;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 协调 SPA 单页面应用和 REST API JSON 数据返回的控制器。
 *
 * @author 吴仙杰
 */
@Controller
@Configuration
public class NotFoundController {

  /**
   * 处理“404 未找到”请求的 URI。
   */
  public static final String URI_NOT_FOUND = "/404";

  /**
   * 处理 REST API 请求的 URI 前缀。
   */
  public static final String URI_PREFIX_API = "/api/";

  /**
   * SPA 首页。
   */
  public static final String PAGE_SPA_INDEX = "classpath:/static/index.html";

  /**
   * 配置 Web 服务器工厂接口：
   *
   * <ul>
   *   <li>将“404 未找”到重定向至自定义控制器 {@link #URI_NOT_FOUND}</li>
   * </ul>
   *
   * @return 自定义配置后的 Web 服务器工厂接口。
   */
  @Bean
  public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
    return factory -> factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, URI_NOT_FOUND));
  }

  /**
   * 404 处理，按规则返回 JSON 数据或页面。
   *
   * <p>返回 JSON 数据：
   *
   * <ol>
   *   <li>请求头 {@link HttpHeaders#ACCEPT} 中存在 {@link MediaType#APPLICATION_JSON_VALUE}</li>
   *   <li>请求 URI 以 {@link #URI_PREFIX_API} 开头</li>
   * </ol>
   *
   * <p>其他情况一律返回页面，因为前端 SPA 单页面应用已作为静态资源打包在了 Jar 中。
   *
   * <p>Spring Boot 默认会将 {@code src/main/resources/static/} 中的内容作为静态资源提供。
   *
   * <p>约定 SPA 的页面入口：{@link #PAGE_SPA_INDEX}。
   *
   * @return JSON 数据或 SPA 首页
   */
  @RequestMapping(URI_NOT_FOUND)
  public ResponseEntity<?> handleNotFoundRequest() {
    // API 或 JSON 数据请求返回 JSON 数据
    final HttpServletRequest request = ServletUtils.getCurrentRequest().orElseThrow();
    final String originalUri = request.getAttribute(RequestDispatcher.FORWARD_SERVLET_PATH).toString();

    if (isJsonRequest(request, originalUri)) {
      return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(new ErrorResponse(HttpStatus.NOT_FOUND, "资源不存在", originalUri));
    }

    // 返回 SPA 首页，由前端处理 404
    return ResponseEntity
      .status(HttpStatus.OK)
      .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
      .body(ResourceUtil.getResourceObj(PAGE_SPA_INDEX).readUtf8Str());
  }

  private boolean isJsonRequest(final HttpServletRequest request, final String originalUri) {
    final String accept = JakartaServletUtil.getHeaderIgnoreCase(request, HttpHeaders.ACCEPT);

    if (StrUtil.containsIgnoreCase(accept, MediaType.APPLICATION_JSON_VALUE)) {
      return true;
    }

    return StrUtil.startWith(originalUri, URI_PREFIX_API);
  }
}
