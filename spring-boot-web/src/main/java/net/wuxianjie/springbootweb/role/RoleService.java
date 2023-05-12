package net.wuxianjie.springbootweb.role;

import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.auth.AuthUtils;
import net.wuxianjie.springbootweb.role.dto.RoleResponse;
import net.wuxianjie.springbootweb.user.UserMapper;
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
}
