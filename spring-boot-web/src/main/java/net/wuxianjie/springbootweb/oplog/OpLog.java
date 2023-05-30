package net.wuxianjie.springbootweb.oplog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 操作日志数据库表实体类。
 *
 * @author 吴仙杰
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpLog {

  /**
   * 操作日志 id。
   */
  private Long id;
  /**
   * 请求时间。
   */
  private Date reqTime;
  /**
   * 客户端 IP。
   */
  private String clientIp;
  /**
   * 用户名。
   */
  private String username;
  /**
   * 操作描述。
   */
  private String message;
}
