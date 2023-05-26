package net.wuxianjie.springbootweb.test;

public class StudentNotFoundException extends RuntimeException {

  public StudentNotFoundException(final String message) {
    super(message);
  }

  public StudentNotFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public StudentNotFoundException(final Throwable cause) {
    super(cause);
  }
}
