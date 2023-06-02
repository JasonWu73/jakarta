package net.wuxianjie.springbootweb.test.securitymvc;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

/**
 * 角色数据库表实体类。
 *
 * @author 吴仙杰
 */
@Entity
@Table(name = "role")
@Data
public class Role {

  /**
   * 角色 id。
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  /**
   * 角色名。
   */
  @Column(name = "name")
  private String name;

  /**
   * 以英文逗号分隔的功能权限字符串。
   */
  @Column(name = "authorities")
  private String authorities;

  /**
   * 父角色 id。
   */
  @Column(name = "parent_id")
  private Long parentId;

  /**
   * 父角色名。
   */
  @Column(name = "parent_name")
  private String parentName;

  /**
   * 节点全路径，以 {@code .} 分隔的角色 id。
   */
  @Column(name = "full_path")
  private String fullPath;

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
