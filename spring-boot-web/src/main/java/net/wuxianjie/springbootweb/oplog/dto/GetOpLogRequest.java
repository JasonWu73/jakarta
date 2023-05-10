package net.wuxianjie.springbootweb.oplog.dto;

import cn.hutool.core.date.DatePattern;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 获取操作日志列表请求参数。
 *
 * @author 吴仙杰
 */
@Data
public class GetOpLogRequest {

  /**
   * 请求起始时间。
   */
  @NotNull(message = "开始时间不能为 null")
  @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
  private LocalDateTime startTime;

  /**
   * 请求结束时间。
   */
  @NotNull(message = "结束时间不能为 null")
  @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
  private LocalDateTime endTime;

  /**
   * 客户端 IP。
   */
  private String clientIp;

  /**
   * 方法描述。
   */
  private String message;
}
