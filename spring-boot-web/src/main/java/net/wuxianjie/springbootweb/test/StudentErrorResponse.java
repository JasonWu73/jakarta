package net.wuxianjie.springbootweb.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentErrorResponse {

  private int status;
  private String message;
  private long timestamp;
}
