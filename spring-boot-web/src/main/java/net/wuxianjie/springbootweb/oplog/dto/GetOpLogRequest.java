package net.wuxianjie.springbootweb.oplog.dto;

import cn.hutool.core.date.DatePattern;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 获取操作日志请求参数。
 *
 * @author 吴仙杰
 */
@Data
public class GetOpLogRequest {

  /**
   * 请求开始时间，必填。
   */
  @NotNull(message = "请求开始时间不能为 null")
  @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
  private LocalDateTime startTime;

  /**
   * 请求结束时间，必填。
   */
  @NotNull(message = "请求结束时间不能为 null")
  @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
  private LocalDateTime endTime;

  /**
   * 客户端 IP。
   */
  private String clientIp;

  /**
   * 操作描述。
   */
  private String message;
}
