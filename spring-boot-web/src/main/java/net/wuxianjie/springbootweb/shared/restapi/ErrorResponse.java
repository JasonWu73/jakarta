package net.wuxianjie.springbootweb.shared.restapi;

import jakarta.servlet.http.HttpServletRequest;
import net.wuxianjie.springbootweb.shared.util.ServletUtils;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * 自定义 API 错误结果。
 *
 * @param timestamp 请求时间戳
 * @param status HTTP 响应状态码
 * @param error 错误提示信息
 * @param path 请求的资源路径
 * @author 吴仙杰
 **/
public record ErrorResponse(LocalDateTime timestamp, int status, String error, String path) {

  /**
   * 构造 API 错误结果。
   *
   * @param status HTTP 响应状态码
   * @param error 错误提示信息
   * @param path 请求的资源路径
   */
  public ErrorResponse(final HttpStatus status, final String error, final String path) {
    this(LocalDateTime.now(), status.value(), error, path);
  }

  /**
   * 构造 API 错误结果。
   *
   * @param status HTTP 响应状态码
   * @param error 错误提示信息
   */
  public ErrorResponse(final HttpStatus status, final String error) {
    this(
      status,
      error,
      ServletUtils.getCurrentRequest().map(HttpServletRequest::getRequestURI).orElse(null)
    );
  }
}
