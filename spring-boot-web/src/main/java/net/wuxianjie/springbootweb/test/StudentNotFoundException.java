package net.wuxianjie.springbootweb.test;

public class StudentNotFoundException extends RuntimeException {

  public StudentNotFoundException(final String message) {
    super(message);
  }
}
