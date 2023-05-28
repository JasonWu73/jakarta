package net.wuxianjie.springbootweb.role;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
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
    // 获取当前用户的角色全路径
    final String roleFullPathPrefix = getCurrentUserRoleFullPathPrefix();

    // 检索数据库，获取当前用户的所有下级角色
    return ResponseEntity.ok(roleMapper.selectByFullPathLikeOrderByUpdatedAtDesc(roleFullPathPrefix));
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
    // 检索数据库，获取角色数据
    final RoleBaseInfo roleInfo = Optional.ofNullable(roleMapper.selectBaseById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到角色数据"));

    // 检验是否为当前用户的下级角色
    if (isNotSubordinateRole(roleInfo.getFullPath())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许查看下级角色");
    }

    return ResponseEntity.ok(roleInfo);
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
    // 检索数据库，检验是否存在同名角色
    checkNameUniqueness(req.getName());

    // 检验父角色是否为当前用户的下级角色
    final Long parentId = req.getParentId();
    final RoleBaseInfo parent;

    if (parentId == null) {
      // 当不指定新增角色的父角色时，即代表使用当前用户的角色作为上级角色
      parent = Optional.ofNullable(roleMapper.selectBaseByUserId(getCurrentUserId())).orElseThrow();
    } else {
      // 检验父角色是否为当前用户的下级角色
      parent = Optional.ofNullable(roleMapper.selectBaseById(parentId))
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到父角色数据"));

      if (isNotSubordinateRole(parent.getFullPath())) {
        throw new ApiException(HttpStatus.FORBIDDEN, "只允许创建下级角色");
      }
    }

    // 检验新增权限是否为当前用户的下级权限，并格式化功能权限字符串（去重、去除空值、字符串的左右空格，及仅保留父权限）
    final String sanitizedAuthorities = toSanitizeAuthorityCommaSeparatorStr(req.getAuthorities());

    // 添加角色到数据库
    final Role roleToSave = new Role();
    roleToSave.setName(req.getName());
    roleToSave.setAuthorities(sanitizedAuthorities);
    roleToSave.setParentId(parent.getId());
    roleToSave.setParentName(parent.getName());
    roleToSave.setCreatedAt(LocalDateTime.now());
    roleToSave.setUpdatedAt(LocalDateTime.now());
    roleToSave.setRemark(req.getRemark());

    // 对于节点全路径还需要拼接当前新增角色的 id
    roleToSave.setFullPath(parent.getFullPath() + StrUtil.DOT);

    roleMapper.insert(roleToSave);

    // 更新数据库中刚新增角色的全路径
    roleToSave.setFullPath(roleToSave.getFullPath() + roleToSave.getId());

    roleMapper.update(roleToSave);

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
    // 检索数据库，获取旧角色数据
    final RoleBaseInfo oldRole = Optional.ofNullable(roleMapper.selectBaseById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到角色数据"));

    // 检验是否为当前用户的下级角色
    if (isNotSubordinateRole(oldRole.getFullPath())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许更新下级角色");
    }

    // 设置需要更新的字段
    final Role roleToUpdate = new Role();
    roleToUpdate.setId(id);
    roleToUpdate.setRemark(req.getRemark());

    // 若需要更新角色名，则检索数据库，检验是否存在同名角色
    if (!StrUtil.equals(oldRole.getName(), req.getName())) {
      roleToUpdate.setName(req.getName());

      // 检验数据库是否已存在同名角色
      checkNameUniqueness(req.getName());

      // 更新其子角色的父角色名
      roleMapper.updateParentNameByParentId(req.getName(), id);
    }

    // 若需要更新父角色，则检验父角色是否合法
    if (!NumberUtil.equals(oldRole.getParentId(), req.getParentId())) {
      roleToUpdate.setParentId(req.getParentId());

      // 检验是否错误地将自身作为父角色
      if (NumberUtil.equals(req.getParentId(), id)) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "不能将自己作为自己的上级");
      }

      // 检验父角色是否存在
      final RoleBaseInfo parent = Optional.ofNullable(roleMapper.selectBaseById(req.getParentId()))
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到父角色数据"));

      roleToUpdate.setParentName(parent.getName());

      // 检验父角色是否为当前用户的下级角色
      if (isNotSubordinateRole(parent.getFullPath())) {
        throw new ApiException(HttpStatus.FORBIDDEN, "父角色不是当前用户的下级角色");
      }

      // 检验父角色是否为需要更新角色的下级角色
      final String oldFullPathPrefix = oldRole.getFullPath() + StrUtil.DOT;

      if (StrUtil.startWith(parent.getFullPath(), oldFullPathPrefix)) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "下级角色不能作为父角色");
      }

      // 更新其所有下级角色的全路径
      roleToUpdate.setFullPath(parent.getFullPath() + StrUtil.DOT + id);

      roleMapper.updateFullPathByFullPathLike(roleToUpdate.getFullPath() + StrUtil.DOT, oldFullPathPrefix);
    }

    // 若需要更新功能权限，则检验功能权限是否合法
    if (!CollUtil.isEqualList(oldRole.getAuthorities(), req.getAuthorities())) {
      // 检验新增权限是否为当前用户的下级权限，并格式化功能权限字符串（去重、去除空值、字符串的左右空格，及仅保留父权限）
      final String sanitizedAuthorities = toSanitizeAuthorityCommaSeparatorStr(req.getAuthorities());

      roleToUpdate.setAuthorities(sanitizedAuthorities);
    }

    // 更新数据库中的角色
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
    // 检索数据库，获取角色
    final RoleBaseInfo roleToDel = Optional.ofNullable(roleMapper.selectBaseById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到角色数据"));

    // 检验是否为当前用户的下级角色
    if (isNotSubordinateRole(roleToDel.getFullPath())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许删除下级角色");
    }

    // 检验需要删除角色是否还存在下级角色
    checkNotExistsSubordinateRole(roleToDel.getFullPath());

    // 检验需要删除的角色是否还关联着用户
    checkNotExistsUser(id);

    // 删除数据库中的用色
    roleMapper.deleteById(id);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 获取当前用户的角色全路径前缀。
   *
   * @return full_path + {@value StrUtil#DOT}
   */
  public String getCurrentUserRoleFullPathPrefix() {
    return Optional.ofNullable(userMapper.selectRoleFullPathById(getCurrentUserId()))
      .map(s -> s + StrUtil.DOT)
      .orElseThrow();
  }

  /**
   * 检查角色全路径是否不是为当前用户的下级角色。
   *
   * @param checkedFullPath 需要检查的角色全路径
   * @return 是否不是为当前用户的下级角色
   */
  public boolean isNotSubordinateRole(final String checkedFullPath) {
    final String currentFullPathPrefix = getCurrentUserRoleFullPathPrefix();

    return !StrUtil.startWith(checkedFullPath, currentFullPathPrefix);
  }

  private long getCurrentUserId() {
    return AuthUtils.getCurrentUser().orElseThrow().getUserId();
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

    // 检验新增权限是否为当前用户的下级权限，并格式化功能权限字符串（去重、去除空值，及字符串的左右空格）
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
        return StrUtil.equals(own, authCode) || Authority.isSubNode(own, authCode);
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
