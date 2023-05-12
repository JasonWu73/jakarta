package net.wuxianjie.springbootweb.role;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.auth.AuthUtils;
import net.wuxianjie.springbootweb.auth.Authority;
import net.wuxianjie.springbootweb.auth.dto.AuthData;
import net.wuxianjie.springbootweb.role.dto.AddRoleRequest;
import net.wuxianjie.springbootweb.role.dto.RoleResponse;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import net.wuxianjie.springbootweb.user.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    return ResponseEntity.ok(roleMapper.selectAllByFullPathLikeOrderByUpdatedAt(subRoleLikeFullPath));
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
    final boolean existsName = roleMapper.existsByName(request.getName());

    if (existsName) {
      throw new ApiException(HttpStatus.CONFLICT, "已存在相同角色名");
    }

    // 父角色存在性校验
    final Role parentRole = Optional.ofNullable(roleMapper.selectById(request.getParentId()))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到父角色数据"));

    // 校验新增角色是否为当前用户的下级角色
    final AuthData currentUser = AuthUtils.getCurrentUser().orElseThrow();

    final String currentRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathById(currentUser.userId()))
      .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "无法获取当前用户的角色信息"));

    if (!parentRole.getFullPath().startsWith(currentRoleFullPath + ".")) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许创建下级角色");
    }

    // 校验新增权限是否为当前用户的下级权限，并格式化功能权限字符串（去除空值，及字符串的左右空格）
    final List<String> currentUserAuthorities = currentUser.authorities();

    final List<String> sanitizedAuthorities = Arrays.stream(request.getAuthorities().split(","))
      .filter(s -> StrUtil.trimToNull(s) != null)
      .map(s -> {
        final String auth = s.trim();

        if (currentUserAuthorities.stream()
          .noneMatch(currentAuth -> {
              try {
                return currentAuth.equals(auth) || Authority.isSubNode(currentAuth, auth);
              } catch (IllegalArgumentException e) {
                throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
              }
            }
          )
        ) {
          throw new ApiException(HttpStatus.BAD_REQUEST, "不能分配超过当前账号权限的权限");
        }

        return auth;
      })
      .toList();

    // 仅需保存父权限即可
    final String savedAuthorities = sanitizedAuthorities.stream()
      .filter(checkedAuth ->
        sanitizedAuthorities.stream()
          .noneMatch(auth -> {
              try {
                return Authority.isSubNode(auth, checkedAuth);
              } catch (IllegalArgumentException e) {
                throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
              }
            }
          )
      )
      .collect(Collectors.joining(","));

    // 保存至数据库
    final Role role = new Role();
    role.setName(request.getName());
    role.setAuthorities(savedAuthorities);
    role.setParentId(request.getParentId());
    role.setParentName(parentRole.getName());
    role.setFullPath(parentRole.getFullPath() + "."); // 还需要拼接当前新增角色的 id
    role.setCreatedAt(LocalDateTime.now());
    role.setUpdatedAt(LocalDateTime.now());
    role.setRemark(request.getRemark());

    if (roleMapper.insert(role) == 0) {
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "新增角色失败");
    }

    // 更新角色的完整路径至数据库
    role.setFullPath(role.getFullPath() + role.getId());

    if (roleMapper.update(role) == 0) {
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "更新角色完整路径失败");
    }

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
