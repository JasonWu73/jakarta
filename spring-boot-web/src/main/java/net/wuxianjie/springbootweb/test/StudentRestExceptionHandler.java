package net.wuxianjie.springbootweb.test;

import cn.hutool.core.date.DateUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class StudentRestExceptionHandler {

  @ExceptionHandler(StudentNotFoundException.class)
  public ResponseEntity<StudentErrorResponse> handleException(final StudentNotFoundException exc) {
    final StudentErrorResponse error = new StudentErrorResponse();

    error.setStatus(HttpStatus.NOT_FOUND.value());
    error.setMessage(exc.getMessage());
    error.setTimestamp(DateUtil.current());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }
}
