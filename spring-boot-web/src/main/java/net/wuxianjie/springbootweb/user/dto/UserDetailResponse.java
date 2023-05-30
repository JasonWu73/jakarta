package net.wuxianjie.springbootweb.user.dto;

import lombok.Data;
import net.wuxianjie.springbootweb.auth.AccountStatus;

import java.util.Date;

/**
 * 用户详情响应数据。
 *
 * @author 吴仙杰
 */
@Data
public class UserDetailResponse {

  /**
   * 用户 id。
   */
  private long id;
  /**
   * 用户名。
   */
  private String username;
  /**
   * 昵称。
   */
  private String nickname;
  /**
   * 账号状态。
   */
  private AccountStatus status;
  /**
   * 角色 id。
   */
  private long roleId;
  /**
   * 角色名。
   */
  private String roleName;
  /**
   * 角色全路径。
   */
  private String fullPath;
  /**
   * 更新时间。
   */
  private Date updatedAt;
  /**
   * 备注。
   */
  private String remark;
}
