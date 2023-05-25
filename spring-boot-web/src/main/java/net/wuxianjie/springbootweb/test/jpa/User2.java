package net.wuxianjie.springbootweb.test.jpa;

import jakarta.persistence.*;
import lombok.Data;
import net.wuxianjie.springbootweb.auth.AccountStatus;

import java.time.LocalDateTime;

/**
 * 用户数据表实体类。
 *
 * @author 吴仙杰
 */
@Entity
@Table(name = "user2")
@Data
public class User2 {

  /**
   * 用户 id。
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  /**
   * 用户名。
   */
  @Column(name = "username")
  private String username;

  /**
   * 昵称。
   */
  @Column(name = "nickname")
  private String nickname;

  /**
   * 编码后的密码。
   */
  @Column(name = "hashed_password")
  private String hashedPassword;

  /**
   * 账号状态。
   */
  @Column(name = "status")
  private AccountStatus status;

  /**
   * 角色 id。
   */
  @Column(name = "role_id")
  private Long roleId;

  /**
   * 创建时间。
   */
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  /**
   * 更新时间。
   */
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  /**
   * 备注。
   */
  @Column(name = "remark")
  private String remark;

  public User2() {
  }

  public User2(final String username, final String nickname, final String hashedPassword, final Long roleId) {
    this.username = username;
    this.nickname = nickname;
    this.hashedPassword = hashedPassword;
    this.roleId = roleId;
    this.status = AccountStatus.ENABLED;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    this.remark = null;
  }
}
