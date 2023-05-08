package net.wuxianjie.springbootweb.shared.restapi;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wuxianjie.springbootweb.shared.util.ServletUtils;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller 层全局异常处理。
 *
 * @author 吴仙杰
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

  /**
   * 处理因客户端请主动终止请求而引发的异常。
   *
   * <p>因为客户端已终止连接，故无需返回值。
   *
   * @param e 因客户端请主动终止请求而引发的异常
   */
  @ExceptionHandler(ClientAbortException.class)
  public void handleException(final ClientAbortException e) {
    logError(new ApiException(HttpStatus.BAD_REQUEST, "客户端主动终止请求", e));
  }

  /**
   * 处理因客户端请求头中指定的 Content-Type 不符合 API 服务所定义的 MIME 类型而引发的异常。
   *
   * @param e {@link HttpMediaTypeException}
   * @param contentType 客户端请求头中指定的 Content-Type
   * @return 自定义 API 错误结果
   */
  @ExceptionHandler(HttpMediaTypeException.class)
  public ResponseEntity<ErrorResponse> handleException(
    final HttpMediaTypeException e,
    @RequestHeader(HttpHeaders.CONTENT_TYPE) final String contentType
  ) {
    return handleApiException(new ApiException(
      HttpStatus.NOT_ACCEPTABLE,
      StrUtil.format(
        "不支持的 MIME 类型 [{}: {}]",
        HttpHeaders.CONTENT_TYPE,
        contentType
      ),
      e
    ));
  }

  /**
   * 处理因客户端请求方法不符合 API 服务所定义的请求方法而引发的异常。
   *
   * @param e {@link HttpRequestMethodNotSupportedException}
   * @param request HTTP servlet 请求信息
   * @return 自定义 API 错误结果
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleException(
    final HttpRequestMethodNotSupportedException e,
    final HttpServletRequest request
  ) {
    return handleApiException(new ApiException(
      HttpStatus.METHOD_NOT_ALLOWED,
      StrUtil.format("不支持的请求方法 [{}]", request.getMethod()),
      e
    ));
  }

  /**
   * 处理因无法解析客户端请求体内容而引发的异常。
   *
   * @param e {@link HttpMessageConversionException}
   * @return 自定义 API 错误结果
   */
  @ExceptionHandler(HttpMessageConversionException.class)
  public ResponseEntity<ErrorResponse> handleException(
    final HttpMessageConversionException e
  ) {
    return handleApiException(new ApiException(
      HttpStatus.BAD_REQUEST,
      "无法解析请求体内容",
      e
    ));
  }

  /**
   * 处理因客户端请求缺少必填参数而引发的异常。
   *
   * @param e {@link MissingServletRequestParameterException}
   * @return 自定义 API 错误结果
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleException(
    final MissingServletRequestParameterException e
  ) {
    return handleApiException(
      new ApiException(
        HttpStatus.BAD_REQUEST,
        StrUtil.format("缺少必填参数 [{}]", e.getParameterName()),
        e
      )
    );
  }

  /**
   * 处理因客户端请求参数不符合 API 定义的校验规则而引发的异常。
   *
   * <p>触发本异常的校验方式：
   *
   * <ul>
   *   <li>Controller 类必须有 {@link Validated} 注解</li>
   *   <li>直接对 Controller 方法参数使用校验注解</li>
   * </ul>
   *
   * @param e {@link ConstraintViolationException}
   * @return 自定义 API 错误结果
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleException(final ConstraintViolationException e) {
    // 获取各个参数的违规信息
    final List<String> errors = new ArrayList<>();

    Optional.ofNullable(e.getConstraintViolations())
      .ifPresent(violations -> violations.forEach(v -> errors.add(v.getMessage())));

    return handleApiException(new ApiException(
      HttpStatus.BAD_REQUEST,
      concatenateErrorMessages(errors),
      e
    ));
  }

  /**
   * 处理因客户端请求参数不符合 API 定义的校验规则而引发的异常。
   *
   * <p>触发本异常的校验方式：
   * <ul>
   *   <li>对方法参数使用 {@link Valid} 注解</li>
   *   <li>方法参数是 POJO 类</li>
   * </ul>
   *
   * @param e {@link BindException}
   * @return 自定义 API 错误结果
   */
  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> handleException(final BindException e) {
    // 获取各个参数的违规信息
    final List<String> errors = new ArrayList<>();

    e.getBindingResult().getFieldErrors().forEach(fieldError -> {
      // 当接收的参数类型不符合要求时，只需提示参数有误即可，而不是返回服务异常
      final boolean isTypeError = fieldError.contains(TypeMismatchException.class);

      final String message = isTypeError ?
        StrUtil.format("参数类型有误 [{}]", fieldError.getField()) :
        fieldError.getDefaultMessage();

      errors.add(message);
    });

    return handleApiException(new ApiException(
      HttpStatus.BAD_REQUEST,
      concatenateErrorMessages(errors),
      e
    ));
  }

  /**
   * 处理因客户端请求参数不符合 Controller 方法参数所定义的类型而引发的异常。
   *
   * @param e {@link MethodArgumentTypeMismatchException}
   * @return 自定义 API 错误结果
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleException(final MethodArgumentTypeMismatchException e) {
    return handleApiException(new ApiException(
      HttpStatus.BAD_REQUEST,
      StrUtil.format(
        "参数值有误 [{}={}]",
        e.getName(),
        e.getValue()
      ),
      e
    ));
  }

  /**
   * 处理所有未被特定 {@code handleException(...)} 方法捕获的异常。
   *
   * @param e {@link Throwable}
   * @return 自定义 API 错误结果
   */
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ErrorResponse> handleException(final Throwable e) {
    // 不要处理 AccessDeniedException，否则会导致 Spring Security 无法处理 403
    if (e instanceof AccessDeniedException springSecurity403Exception) {
      throw springSecurity403Exception;
    }

    return handleApiException(new ApiException(
      HttpStatus.INTERNAL_SERVER_ERROR,
      "服务异常",
      e,
      true
    ));
  }

  /**
   * 处理自定义 API 异常。
   *
   * @param e 自定义 API 异常
   * @return 自定义 API 错误结果
   */
  @ExceptionHandler(ApiException.class)
  private ResponseEntity<ErrorResponse> handleApiException(final ApiException e) {
    logError(e);

    return ResponseEntity
      .status(e.getStatus())
      .body(new ErrorResponse(e.getStatus(), e.getReason()));
  }

  private void logError(final ApiException e) {
    // 以 WARN 级别记录客户端异常
    final boolean isClientError = e.getStatus().is4xxClientError();
    final String clientInfo = ServletUtils.getClientInfo();

    if (isClientError && e.isLogStack()) {
      log.warn("{} -> {}", clientInfo, e.getMessage(), e);
      return;
    }

    if (isClientError) {
      log.warn("{} -> {}", clientInfo, e.getMessage());
      return;
    }

    // 以 ERROR 级别记录服务器端异常
    if (e.isLogStack()) {
      log.error("{} -> {}", clientInfo, e.getMessage(), e);
      return;
    }

    log.error("{} -> {}", clientInfo, e.getMessage());
  }

  private static String concatenateErrorMessages(final List<String> errorMessages) {
    return String.join("; ", errorMessages);
  }
}
