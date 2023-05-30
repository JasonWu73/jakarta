package net.wuxianjie.springbootweb.shared.restapi;

import cn.hutool.core.date.DateTime;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.wuxianjie.springbootweb.shared.util.ServletUtils;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * 自定义 API 错误结果。
 *
 * @author 吴仙杰
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

  /**
   * 请求的时间戳。
   */
  private Date timestamp;
  /**
   * HTTP 响应状态码。
   */
  private int status;
  /**
   * 错误提示信息。
   */
  private String error;
  /**
   * 请求的资源路径。
   */
  private String path;

  /**
   * 构造 API 错误结果。
   *
   * @param status HTTP 响应状态码
   * @param error 错误提示信息
   * @param path 请求的资源路径
   */
  public ErrorResponse(final HttpStatus status, final String error, final String path) {
    this(DateTime.now(), status.value(), error, path);
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
