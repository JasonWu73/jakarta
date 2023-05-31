package net.wuxianjie.springbootweb.shared.spa;

import cn.hutool.core.io.resource.NoResourceException;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Optional;

/**
 * 兼容 SPA 单页面应用和 REST API 服务。
 *
 * @author 吴仙杰
 */
@Controller
@Configuration
@Slf4j
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
  public static final String SPA_INDEX_PAGE = "classpath:/static/index.html";

  /**
   * 配置 Web 服务器工厂：
   *
   * <ul>
   *   <li>将“404 未找”到重定向至自定义 Controller {@value #URI_NOT_FOUND}</li>
   * </ul>
   *
   * @return 自定义配置后的 Web 服务器工厂。
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
   *   <li>请求头 {@value HttpHeaders#ACCEPT} 中存在 {@value MediaType#APPLICATION_JSON_VALUE}</li>
   *   <li>请求 URI 以 {@value #URI_PREFIX_API} 开头</li>
   * </ol>
   *
   * <p>其他情况一律返回页面，因为前端 SPA 单页面应用已作为静态资源打包在了 Jar 中。
   *
   * <p>Spring Boot 默认会将 {@code src/main/resources/static/} 中的内容作为 Web 静态资源提供。
   *
   * <p>约定 SPA 的页面入口：{@value #SPA_INDEX_PAGE}。
   *
   * <h2>扩展说明</h2>
   *
   * <p>Spring Boot Web 静态资源查找目录，由优先级高到低排序：
   *
   * <ol>
   *   <li>{@code src/main/resources/META-INF/resources/}</li>
   *   <li>{@code src/main/resources/resources/}</li>
   *   <li>{@code src/main/resources/static/}</li>
   *   <li>{@code src/main/resources/public/}</li>
   * </ol>
   *
   * @return JSON 数据或 SPA 首页
   */
  @RequestMapping(URI_NOT_FOUND)
  public ResponseEntity<?> handleNotFoundRequest() {
    // 若 API 或 JSON 数据请求，则返回 JSON 数据
    final HttpServletRequest req = ServletUtils.getCurrentRequest().orElseThrow();
    final String originalUri = Optional.ofNullable(req.getAttribute(RequestDispatcher.FORWARD_SERVLET_PATH))
      .map(Object::toString)
      .orElse("");

    if (isJsonRequest(req, originalUri)) {
      return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(new ErrorResponse(HttpStatus.NOT_FOUND, "未找到请求的资源", originalUri));
    }

    // 返回 SPA 首页，由前端处理 404
    final Resource spaIndexPage;
    try {
      spaIndexPage = ResourceUtil.getResourceObj(SPA_INDEX_PAGE);
    } catch (NoResourceException e) {
      log.warn("SPA 首页 [{}] 不存在: {}", SPA_INDEX_PAGE, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    return ResponseEntity
      .status(HttpStatus.OK)
      .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
      .body(spaIndexPage.readUtf8Str());
  }

  private boolean isJsonRequest(final HttpServletRequest request, final String originalUri) {
    final String accept = JakartaServletUtil.getHeaderIgnoreCase(request, HttpHeaders.ACCEPT);

    if (StrUtil.containsIgnoreCase(accept, MediaType.APPLICATION_JSON_VALUE)) {
      return true;
    }

    return StrUtil.startWith(originalUri, URI_PREFIX_API);
  }
}
