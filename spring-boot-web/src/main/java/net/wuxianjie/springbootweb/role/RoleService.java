package net.wuxianjie.springbootweb.role;

import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.auth.AuthUtils;
import net.wuxianjie.springbootweb.role.dto.AddRoleRequest;
import net.wuxianjie.springbootweb.role.dto.RoleResponse;
import net.wuxianjie.springbootweb.user.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 角色管理相关处理。
 *
 * @author 吴仙杰
 */
@Service
@RequiredArgsConstructor
public class RoleService {

  private final UserMapper userMapper;
  private final RoleMapper roleMapper;

  /**
   * 获取角色列表。
   *
   * <p>用户仅可查看其下级角色。
   *
   * @return 角色列表
   */
  public ResponseEntity<List<RoleResponse>> getRoles() {
    // 获取当前登录用户的角色完整路径以便查找其下级角色
    final long userId = AuthUtils.getCurrentUser().orElseThrow().userId();
    final String roleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathById(userId)).orElseThrow();
    final String subRoleLikeFullPath = roleFullPath + ".%";

    // 查询数据库获取列表数据
    return ResponseEntity.ok(roleMapper.selectAllByFullPathLike(subRoleLikeFullPath));
  }

  /**
   * 新增角色。
   *
   * <p>只允许新增当前用户的下级角色。
   *
   * @param request 请求参数
   * @return 201 HTTP 状态码
   */
  public ResponseEntity<Void> addRole(final AddRoleRequest request) {
    // 角色名唯一性校验

    // 设置的功能权限合法性校验，并格式化功能权限字符串（仅保存最父权限）

    // 父角色有效性校验

    // 校验新增角色是否为当前用户的下级角色

    // 保存至数据库

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
