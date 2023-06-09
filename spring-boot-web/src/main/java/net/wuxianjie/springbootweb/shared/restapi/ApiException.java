package net.wuxianjie.springbootweb.shared.restapi;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义 API 异常。
 *
 * @author 吴仙杰
 **/
public class ApiException extends RuntimeException {

  /**
   * HTTP 响应状态码。
   */
  @Getter
  private final HttpStatus status;
  /**
   * 异常原因。
   */
  @Getter
  private final String reason;
  /**
   * 是否记录异常堆栈信息。
   */
  @Getter
  private final boolean logStack;

  /**
   * 构造异常对象。
   *
   * @param status HTTP 响应状态码
   * @param reason 异常原因
   * @param logStack 是否记录异常堆栈信息
   */
  public ApiException(final HttpStatus status, final String reason, final boolean logStack) {
    super(reason);
    this.status = status;
    this.reason = reason;
    this.logStack = logStack;
  }

  /**
   * 构造无需记录异常堆栈信息的异常对象。
   *
   * @param status HTTP 响应状态码
   * @param reason 异常原因
   */
  public ApiException(final HttpStatus status, final String reason) {
    this(status, reason, false);
  }

  /**
   * 构造异常对象。
   *
   * @param status HTTP 响应状态码
   * @param reason 异常原因
   * @param cause 导致此异常发生的上级异常
   * @param logStack 是否记录异常堆栈信息
   */
  public ApiException(final HttpStatus status, final String reason, final Throwable cause, final boolean logStack) {
    super(reason, cause);
    this.status = status;
    this.reason = reason;
    this.logStack = logStack;
  }

  /**
   * 构造无需记录异常堆栈信息的异常对象。
   *
   * @param status HTTP 响应状态码
   * @param reason 异常原因
   * @param cause 导致此异常发生的上级异常
   */
  public ApiException(final HttpStatus status, final String reason, final Throwable cause) {
    this(status, reason, cause, false);
  }

  /**
   * 获取包含上级异常信息的完整异常信息。
   *
   * @return 完整的异常信息
   */
  @Override
  public String getMessage() {
    final String respMsg = StrUtil.format("{} \"{}\"", status, reason);

    final List<String> msgList = new ArrayList<>();

    msgList.add(respMsg);

    Throwable cause = getCause();

    while (cause != null) {
      final String nestedMsg = StrUtil.format("嵌套异常 [{}: {}]", cause.getClass().getName(), cause.getMessage());

      msgList.add(nestedMsg);

      cause = cause.getCause();
    }

    return StrUtil.join("; ", msgList);
  }
}
