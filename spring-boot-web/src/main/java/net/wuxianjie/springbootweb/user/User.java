package net.wuxianjie.springbootweb.user;

import lombok.Data;
import net.wuxianjie.springbootweb.auth.AccountStatus;

import java.time.LocalDateTime;

/**
 * 用户数据库表实体类。
 *
 * @author 吴仙杰
 */
@Data
public class User {

  /**
   * 用户 id。
   */
  private Long id;

  /**
   * 用户名。
   */
  private String username;

  /**
   * 昵称。
   */
  private String nickname;

  /**
   * 编码后的密码。
   */
  private String hashedPassword;

  /**
   * 账号状态。
   */
  private AccountStatus status;

  /**
   * 角色 id。
   */
  private Long roleId;

  /**
   * 创建时间。
   */
  private LocalDateTime createdAt;

  /**
   * 更新时间。
   */
  private LocalDateTime updatedAt;

  /**
   * 备注。
   */
  private String remark;
}
