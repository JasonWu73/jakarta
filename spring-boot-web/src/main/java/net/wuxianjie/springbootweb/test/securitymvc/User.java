package net.wuxianjie.springbootweb.test.securitymvc;

import jakarta.persistence.*;
import lombok.Data;
import net.wuxianjie.springbootweb.auth.AccountStatus;

import java.util.Date;

/**
 * 用户数据库表实体类。
 *
 * @author 吴仙杰
 */
@Entity
@Table(name = "user")
@Data
public class User {

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
   * 哈希密码。
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
  @ManyToOne()
  @JoinColumn(name = "role_id")
  private Role role;

  /**
   * 创建时间。
   */
  @Column(name = "created_at")
  private Date createdAt;

  /**
   * 更新时间。
   */
  @Column(name = "updated_at")
  private Date updatedAt;

  /**
   * 备注。
   */
  @Column(name = "remark")
  private String remark;
}
