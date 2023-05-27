package net.wuxianjie.springbootweb.role;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.auth.AuthUtils;
import net.wuxianjie.springbootweb.auth.Authority;
import net.wuxianjie.springbootweb.role.dto.AddRoleRequest;
import net.wuxianjie.springbootweb.role.dto.RoleBaseInfo;
import net.wuxianjie.springbootweb.role.dto.RoleItemResponse;
import net.wuxianjie.springbootweb.role.dto.UpdateRoleRequest;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import net.wuxianjie.springbootweb.user.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色相关处理。
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
  public ResponseEntity<List<RoleItemResponse>> getRoles() {
    // 获取当前登录用户的角色全路径
    final String fullPathPrefix = getCurrentUserRoleFullPathPrefix();

    // 从数据库中获取当前登录用户的所有下级角色
    return ResponseEntity.ok(roleMapper.selectByFullPathLikeOrderByUpdatedAtDesc(fullPathPrefix));
  }

  /**
   * 获取角色详情。
   *
   * <p>用户仅可查看其下级角色。
   *
   * @param id 需要查找的角色 id
   * @return 角色详情数据
   */
  public ResponseEntity<RoleBaseInfo> getRoleDetail(final long id) {
    // 从数据库中获取角色数据
    final RoleBaseInfo role = Optional.ofNullable(roleMapper.selectBaseById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到角色数据"));

    // 判断要查看详情的角色是否为当前登录用户的下级角色
    if (isNotSubordinateRole(role.getFullPath())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许查看下级角色");
    }

    return ResponseEntity.ok(role);
  }

  /**
   * 新增角色。
   *
   * <p>用户仅可创建其下级角色。
   *
   * @param req 请求参数
   * @return 201 HTTP 状态码
   */
  @Transactional(rollbackFor = Exception.class)
  public ResponseEntity<Void> addRole(final AddRoleRequest req) {
    // 判断数据库是否已存在同名角色
    checkNameUniqueness(req.getName());

    // 判断是否设置了新增角色的父角色
    final Long parentId = req.getParentId();
    final RoleBaseInfo parent;

    if (parentId == null) {
      // 当不指定新增角色的父角色时，即代表使用当前用户的角色作为上级角色
      parent = Optional.ofNullable(roleMapper.selectBaseByUserId(getCurrentUserId())).orElseThrow();
    } else {
      // 用户仅可创建其角色的下级角色
      parent = Optional.ofNullable(roleMapper.selectBaseById(parentId))
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到父角色数据"));

      if (isNotSubordinateRole(parent.getFullPath())) {
        throw new ApiException(HttpStatus.FORBIDDEN, "只允许创建下级角色");
      }
    }

    // 判断新增权限是否为当前用户的下级权限，并格式化功能权限字符串（去重、去除空值、字符串的左右空格，及仅保留父权限）
    final String sanitizedAuthorities = toSanitizeAuthorityCommaSeparatorStr(req.getAuthorities());

    // 插入数据库
    final Role role = new Role();
    role.setName(req.getName());
    role.setAuthorities(sanitizedAuthorities);
    role.setParentId(parent.getId());
    role.setParentName(parent.getName());

    // 对于节点全路径还需要拼接当前新增角色的 id
    role.setFullPath(parent.getFullPath() + StrUtil.DOT);

    role.setCreatedAt(LocalDateTime.now());
    role.setUpdatedAt(LocalDateTime.now());
    role.setRemark(req.getRemark());

    roleMapper.insert(role);

    // 更新数据库中刚新增角色的全路径
    role.setFullPath(role.getFullPath() + role.getId());

    roleMapper.update(role);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * 更新角色。
   *
   * <p>用户仅可更新其下级角色。
   *
   * @param id 需要更新的角色 id
   * @param req 请求参数
   * @return 204 HTTP 状态码
   */
  @Transactional(rollbackFor = Exception.class)
  public ResponseEntity<Void> updateRole(final long id, final UpdateRoleRequest req) {
    // 判断需要更新的角色是否存在
    final RoleBaseInfo oldRole = Optional.ofNullable(roleMapper.selectBaseById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到角色数据"));

    // 判断需要更新的角色是否为当前登录用户的下级角色
    if (isNotSubordinateRole(oldRole.getFullPath())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许更新下级角色");
    }

    // 判断是否更新角色名
    final Role roleToUpdate = new Role();
    roleToUpdate.setId(id);

    if (!Objects.equals(oldRole.getName(), req.getName())) {
      roleToUpdate.setName(req.getName());

      // 判断数据库是否已存在同名角色
      checkNameUniqueness(req.getName());

      // 更新其子角色的父角色名
      roleMapper.updateParentNameByParentId(req.getName(), id);
    }

    // 判断是否需要更新父角色
    if (!Objects.equals(oldRole.getParentId(), req.getParentId())) {
      roleToUpdate.setParentId(req.getParentId());

      // 判断是否将自身作为了父角色
      if (Objects.equals(req.getParentId(), id)) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "不能将自己作为自己的上级");
      }

      // 判断用于更新的父角色是否存在
      final RoleBaseInfo parent = Optional.ofNullable(roleMapper.selectBaseById(req.getParentId()))
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到父角色数据"));

      roleToUpdate.setParentName(parent.getName());

      // 判断用于更新的父角色是否为当前登录用户角色的下级角色
      if (isNotSubordinateRole(parent.getFullPath())) {
        throw new ApiException(HttpStatus.FORBIDDEN, "父角色不是当前用户的下级角色");
      }

      // 判断用于更新的父角色是否为需要更新的角色的下级角色
      final String oldFullPathPrefix = oldRole.getFullPath() + StrUtil.DOT;

      if (parent.getFullPath().startsWith(oldFullPathPrefix)) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "下级角色不能作为父角色");
      }

      // 更新其所有下级角色的全路径
      roleToUpdate.setFullPath(parent.getFullPath() + StrUtil.DOT + id);

      roleMapper.updateFullPathByFullPathLike(roleToUpdate.getFullPath() + StrUtil.DOT, oldFullPathPrefix);
    }

    // 判断是否需要更新功能权限
    if (!Objects.equals(oldRole.getAuthorities(), req.getAuthorities())) {
      // 判断新增权限是否为当前用户的下级权限，并格式化功能权限字符串（去重、去除空值、字符串的左右空格，及仅保留父权限）
      final String sanitizedAuthorities = toSanitizeAuthorityCommaSeparatorStr(req.getAuthorities());

      roleToUpdate.setAuthorities(sanitizedAuthorities);
    }

    // 更新数据库中的角色
    roleToUpdate.setRemark(req.getRemark());

    roleMapper.update(roleToUpdate);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 删除角色。
   *
   * <p>用户仅可删除其下级角色。
   *
   * @param id 需要删除的角色 id
   * @return 204 HTTP 状态码
   */
  public ResponseEntity<Void> deleteRole(final long id) {
    // 判断需要删除的角色是否存在
    final RoleBaseInfo role = Optional.ofNullable(roleMapper.selectBaseById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到角色数据"));

    // 判断需要删除的角色是否为当前用户角色的下级角色
    if (isNotSubordinateRole(role.getFullPath())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许删除下级角色");
    }

    // 判断需要删除的角色是否还存在下级角色
    checkNotExistsSubordinateRole(role.getFullPath());

    // 判断需要删除的角色是否关联着用户
    checkNotExistsUser(id);

    // 删除数据库中的用色
    roleMapper.deleteById(id);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  private long getCurrentUserId() {
    return AuthUtils.getCurrentUser().orElseThrow().getUserId();
  }

  private String getCurrentUserRoleFullPathPrefix() {
    return Optional.ofNullable(userMapper.selectRoleFullPathById(getCurrentUserId()))
      .map(s -> s + StrUtil.DOT)
      .orElseThrow();
  }

  private boolean isNotSubordinateRole(final String checkFullPath) {
    final String fullPathPrefix = getCurrentUserRoleFullPathPrefix();

    return !checkFullPath.startsWith(fullPathPrefix);
  }

  private void checkNameUniqueness(final String roleName) {
    final boolean existsName = roleMapper.existsRoleByName(roleName);

    if (existsName) {
      throw new ApiException(HttpStatus.CONFLICT, "已存在相同角色名");
    }
  }

  private String toSanitizeAuthorityCommaSeparatorStr(final List<String> newAuthorities) {
    if (newAuthorities == null) {
      // 为 null 代表客户端未填，故无需更新数据库
      return null;
    }

    if (newAuthorities.isEmpty()) {
      // 为空代表是客户端有意为之，故需要更新数据库
      return "";
    }

    // 判断新增权限是否为当前用户的下级权限，并格式化功能权限字符串（去重、去除空值，及字符串的左右空格）
    final List<String> sanitizedAuthorities = newAuthorities.stream()
      .filter(s -> StrUtil.trimToNull(s) != null)
      .map(s -> {
        final String auth = s.trim();

        checkIsASubordinateAuthority(auth);

        return auth;
      })
      .distinct()
      .toList();

    // 仅需保存父权限即可
    return sanitizedAuthorities.stream()
      .filter(checkedAuth -> sanitizedAuthorities.stream()
        .noneMatch(auth -> {
          try {
            return Authority.isSubNode(auth, checkedAuth);
          } catch (IllegalArgumentException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "权限代码不合法", e);
          }
        })
      )
      .collect(Collectors.joining(","));
  }

  private void checkIsASubordinateAuthority(final String authCode) {
    final List<String> ownedAuthorities = AuthUtils.getCurrentUser().orElseThrow().getAuthorities();

    final boolean isIllegal = ownedAuthorities.stream().noneMatch(own -> {
      try {
        // 本级或下级
        return Objects.equals(own, authCode) || Authority.isSubNode(own, authCode);
      } catch (IllegalArgumentException e) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "权限代码不合法", e);
      }
    });

    if (isIllegal) {
      throw new ApiException(HttpStatus.FORBIDDEN, "不能分配超过当前账号功能权限的功能");
    }
  }

  private void checkNotExistsSubordinateRole(final String fullPath) {
    final boolean existsSub = roleMapper.existsRoleByFullPathLike(fullPath + ".%");

    if (existsSub) {
      throw new ApiException(HttpStatus.FORBIDDEN, "不可删除还存在下级的角色");
    }
  }

  private void checkNotExistsUser(final long roleId) {
    final boolean existsUser = roleMapper.existsUserByRoleId(roleId);

    if (existsUser) {
      throw new ApiException(HttpStatus.FORBIDDEN, "不可删除已关联用户的角色");
    }
  }
}
