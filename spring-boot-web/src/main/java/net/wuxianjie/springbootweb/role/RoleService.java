package net.wuxianjie.springbootweb.role;

import cn.hutool.core.util.StrUtil;
import net.wuxianjie.springbootweb.auth.AuthUtils;
import net.wuxianjie.springbootweb.auth.Authority;
import net.wuxianjie.springbootweb.auth.dto.AuthData;
import net.wuxianjie.springbootweb.role.dto.AddRoleRequest;
import net.wuxianjie.springbootweb.role.dto.RoleDetailResponse;
import net.wuxianjie.springbootweb.role.dto.RoleResponse;
import net.wuxianjie.springbootweb.role.dto.UpdateRoleRequest;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import net.wuxianjie.springbootweb.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    final long userId = AuthUtils.getCurrentUser().orElseThrow().getUserId();
    final String roleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathById(userId)).orElseThrow();
    final String subRoleLikeFullPath = roleFullPath + ".%";

    // 查询数据库获取列表数据
    return ResponseEntity.ok(roleMapper.selectAllByFullPathLikeOrderByUpdatedAt(subRoleLikeFullPath));
  }

  /**
   * 获取角色详情。
   *
   * <p>用户仅可查看其下级角色。
   *
   * @param id 需要查找的角色 id
   * @return 角色详情
   */
  public ResponseEntity<RoleDetailResponse> getRoleDetail(final long id) {
    // 根据角色 id 获取详情数据，当数据不存在时抛出 404 异常
    final RoleDetailResponse selectedRole = roleMapper.selectRoleDetailById(id);

    // 当查看的角色不是当前用户所有拥有角色的下级角色时，抛出 403 异常
    if (isNotSubordinateRole(selectedRole.fullPath())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许查看下级角色");
    }

    return ResponseEntity.ok(selectedRole);
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

    // 获取当前登录用户
    final AuthData currentUser = AuthUtils.getCurrentUser().orElseThrow();

    // 父角色存在性校验
    final Role parentRole;
    if (request.getParentId() != null) {
      parentRole = Optional.ofNullable(roleMapper.selectById(request.getParentId()))
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到父角色数据"));

      // 校验新增角色是否为当前用户的下级角色
      final String currentRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathById(currentUser.getUserId()))
        .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "无法获取当前用户的角色信息"));

      if (!parentRole.getFullPath().startsWith(currentRoleFullPath + ".")) {
        throw new ApiException(HttpStatus.FORBIDDEN, "只允许创建下级角色");
      }
    } else {
      // 若父角色 id 为 null，则表示创建自己的下级角色
      parentRole = Optional.ofNullable(roleMapper.selectByUserId(currentUser.getUserId())).orElseThrow();
    }

    // 校验新增权限是否为当前用户的下级权限，并格式化功能权限字符串（去除空值、字符串的左右空格，及仅保留父权限）
    final String sanitizedAuthorities = sanitizeAuthorities(currentUser.getAuthorities(), request.getAuthorities());

    // 保存至数据库
    final Role role = new Role();
    role.setName(request.getName());
    role.setAuthorities(sanitizedAuthorities);
    role.setParentId(parentRole.getId());
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

  /**
   * 更新角色。
   *
   * <p>只允许更新当前角色的下级角色。
   *
   * @param id 需要更新的角色 id
   * @param request 请求参数
   * @return 204 HTTP 状态码
   */
  @Transactional(rollbackFor = Exception.class)
  public ResponseEntity<Void> updateRole(final long id, final UpdateRoleRequest request) {
    // 角色存在性校验
    final Role updatedRole = Optional.ofNullable(roleMapper.selectById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到要更新的角色"));

    // 校验角色是否为当前用户的下级角色
    final AuthData currentUser = AuthUtils.getCurrentUser().orElseThrow();

    final String currentRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathById(currentUser.getUserId()))
      .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "无法获取当前用户的角色信息"));

    if (!updatedRole.getFullPath().startsWith(currentRoleFullPath + ".")) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许更新下级角色");
    }

    // 判断是否更新角色名
    if (!Objects.equals(updatedRole.getName(), request.getName())) {
      // 角色名唯一性校验
      final boolean existsName = roleMapper.existsByName(request.getName());

      if (existsName) {
        throw new ApiException(HttpStatus.CONFLICT, "已存在相同角色名");
      }

      updatedRole.setName(request.getName());

      // 更新下级的父角色名
      roleMapper.updateParentNameByParentId(request.getName(), id);
    }

    // 判断是否需要更新父角色
    if (!Objects.equals(updatedRole.getParentId(), request.getParentId())) {
      // 校验父角色不能是自己
      if (Objects.equals(request.getParentId(), id)) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "不能将自己作为自己的上级");
      }

      // 父角色存在性校验
      final Role parentRole = Optional.ofNullable(roleMapper.selectById(request.getParentId()))
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到父角色数据"));

      // 校验更新的父目标角色是否为当前用户的下级角色
      if (!parentRole.getFullPath().startsWith(currentRoleFullPath + ".")) {
        throw new ApiException(HttpStatus.FORBIDDEN, "父角色只允许更新为当前用户的下级角色");
      }

      // 校验更新的父目标角色不能是要更新角色的下级角色
      if (parentRole.getFullPath().startsWith(updatedRole.getFullPath() + ".")) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "下级角色不能作为父角色");
      }

      updatedRole.setParentId(parentRole.getId());
      updatedRole.setParentName(parentRole.getName());

      // 更新所有下级角色的完整路径
      final String oldFullPath = updatedRole.getFullPath() + ".";
      updatedRole.setFullPath(parentRole.getFullPath() + "." + id);

      // 因为可能不存在下级，故无需通过返回的更新记录数判断是否更新成功
      roleMapper.updateSubFullPath(updatedRole.getFullPath() + ".", oldFullPath);
    }

    // 判断是否需要更新权限
    if (!Objects.equals(updatedRole.getAuthorities(), request.getAuthorities())) {
      // 校验新增权限是否为当前用户的下级权限，并格式化功能权限字符串（去除空值、字符串的左右空格，及仅保留父权限）
      final String sanitizedAuthorities = sanitizeAuthorities(currentUser.getAuthorities(), request.getAuthorities());
      updatedRole.setAuthorities(sanitizedAuthorities);
    }

    // 更新至数据库
    updatedRole.setUpdatedAt(LocalDateTime.now());
    updatedRole.setRemark(request.getRemark());

    if (roleMapper.update(updatedRole) == 0) {
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "更新角色失败");
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 删除角色。
   *
   * <p>只允许更新当前用户的下级角色。
   *
   * @param id 需要删除的角色 id
   * @return 204 HTTP 状态码
   */
  public ResponseEntity<Void> deleteRole(final long id) {
    // 角色存在性校验
    final Role role = Optional.ofNullable(roleMapper.selectById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到要删除的角色"));

    // 校验角色是否为当前用户的下级角色
    final AuthData currentUser = AuthUtils.getCurrentUser().orElseThrow();

    final String currentRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathById(currentUser.getUserId()))
      .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "无法获取当前用户的角色信息"));

    if (!role.getFullPath().startsWith(currentRoleFullPath + ".")) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许删除下级角色");
    }

    // 不可删除存在下级的角色
    final boolean existsSub = roleMapper.existsByFullPathLike(role.getFullPath() + ".%");

    if (existsSub) {
      throw new ApiException(HttpStatus.FORBIDDEN, "不可删除存在下级的角色");
    }

    // 不可删除已被用户绑定的角色
    final boolean existsUser = roleMapper.existsUserById(id);

    if (existsUser) {
      throw new ApiException(HttpStatus.FORBIDDEN, "不可删除已被用户绑定的角色");
    }

    // 从数据库中删除
    if (roleMapper.deleteById(id) == 0) {
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "删除角色失败");
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  private String sanitizeAuthorities(final List<String> currentUserAuthorities, final String newAuthorities) {
    if (newAuthorities == null) {
      return null;
    }

    // 校验新增权限是否为当前用户的下级权限，并格式化功能权限字符串（去除空值，及字符串的左右空格）
    final List<String> sanitizedAuthorities = Arrays.stream(newAuthorities.split(","))
      .filter(s -> StrUtil.trimToNull(s) != null)
      .map(s -> {
        final String auth = s.trim();

        if (currentUserAuthorities.stream()
          .noneMatch(currentAuth -> {
              try {
                return currentAuth.equals(auth) || Authority.isSubNode(currentAuth, auth);
              } catch (IllegalArgumentException e) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "权限代码不合法", e);
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
    return sanitizedAuthorities.stream()
      .filter(checkedAuth ->
        sanitizedAuthorities.stream()
          .noneMatch(auth -> {
              try {
                return Authority.isSubNode(auth, checkedAuth);
              } catch (IllegalArgumentException e) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "权限代码不合法", e);
              }
            }
          )
      )
      .collect(Collectors.joining(","));
  }

  private boolean isNotSubordinateRole(final String subordinateRoleFullPath) {
    final AuthData currentUser = AuthUtils.getCurrentUser().orElseThrow();
    final String currentRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathById(currentUser.getUserId()))
      .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "无法获取当前用户的角色信息"));
    return !subordinateRoleFullPath.startsWith(currentRoleFullPath + ".");
  }
}
