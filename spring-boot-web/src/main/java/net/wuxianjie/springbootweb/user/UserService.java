package net.wuxianjie.springbootweb.user;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.auth.AccountStatus;
import net.wuxianjie.springbootweb.auth.AuthUtils;
import net.wuxianjie.springbootweb.role.RoleService;
import net.wuxianjie.springbootweb.shared.pagination.PaginationParam;
import net.wuxianjie.springbootweb.shared.pagination.PaginationResult;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import net.wuxianjie.springbootweb.shared.util.StrUtils;
import net.wuxianjie.springbootweb.user.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户管理相关处理。
 *
 * @author 吴仙杰
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final RoleService roleService;

  /**
   * 获取用户分页列表。
   *
   * <p>用户仅可查看其下级角色的用户。
   *
   * @param pag 分页参数
   * @param query 查询参数
   * @return 用户分页列表
   */
  public ResponseEntity<PaginationResult<UserItemResponse>> getUsers(
    final PaginationParam pag,
    final GetUserRequest query
  ) {
    // 设置模糊查询参数
    setFuzzyQueryParams(query);

    // 获取当前用户的角色全路径
    final String roleFullPathPrefix = roleService.getCurrentUserRoleFullPathPrefix();

    // 检索数据库，获取用户分页列表，并按更新时间降序排列
    final List<UserItemResponse> list = userMapper.selectByQueryOrderByUpdatedAtDescLimit(pag, query, roleFullPathPrefix);

    // 检索数据库，获取用户总条数
    final long total = userMapper.countByQuery(query, roleFullPathPrefix);

    // 构造分页查询结果
    return ResponseEntity.ok(new PaginationResult<>(
      pag.getPageNum(),
      pag.getPageSize(),
      total,
      list
    ));
  }

  /**
   * 获取用户详情。
   *
   * <p>用户仅可查看其下级角色的用户。
   *
   * @param id 需要查找的用户 id
   * @return 用户详情数据
   */
  public ResponseEntity<UserDetailResponse> getUserDetail(final long id) {
    // 检索数据库，获取用户数据
    final UserDetailResponse userInfo = Optional.ofNullable(userMapper.selectUserDetailById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到用户数据"));

    // 检验是否为当前用户的下级用户
    if (roleService.isNotSubordinateRole(userInfo.getFullPath())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许查看下级用户");
    }

    return ResponseEntity.ok(userInfo);
  }

  /**
   * 新增用户。
   *
   * <p>用户仅可创建其下级角色的用户。
   *
   * @param req 请求参数
   * @return 201 HTTP 状态码
   */
  public ResponseEntity<Void> addUser(final AddUserRequest req) {
    // 检索数据库，检验是否存在相同用户名的用户
    checkNameUniqueness(req.getUsername());

    // 检验是否为当前用户的下级用户
    final String oldRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathByRoleId(req.getRoleId()))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到角色数据"));

    if (isNotSubordinateRole(oldRoleFullPath)) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许创建下级用户");
    }

    // 将明文密码进行 Hash 计算后再保存
    final String hashedPassword = passwordEncoder.encode(req.getPassword());

    // 保存至数据库
    final User user = new User();
    user.setUsername(req.getUsername());
    user.setNickname(req.getNickname());
    user.setHashedPassword(hashedPassword);
    user.setStatus(AccountStatus.resolve(req.getStatus()).orElseThrow());
    user.setRoleId(req.getRoleId());
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    user.setRemark(req.getRemark());

    userMapper.insert(user);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * 更新用户。
   *
   * <p>用户仅可更新其下级角色的用户。
   *
   * @param id 需要更新的用户 id
   * @param req 请求参数
   * @return 204 HTTP 状态码
   */
  public ResponseEntity<Void> updateUser(final long id, final UpdateUserRequest req) {
    // 检索数据库，获取旧用户数据
    final UserBaseInfo oldUser = Optional.ofNullable(userMapper.selectBaseById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到用户数据"));

    // 检验是否为当前用户的下级用户
    if (isNotSubordinateRole(oldUser.getRoleId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许更新下级用户");
    }

    // 设置需要更新的字段
    final User userToUpdate = new User();
    userToUpdate.setId(id);
    userToUpdate.setNickname(req.getNickname());
    userToUpdate.setStatus(AccountStatus.resolve(req.getStatus()).orElseThrow());
    userToUpdate.setRemark(req.getRemark());

    // 若需要更新角色，则检验是否为当前用户的下级角色
    if (!NumberUtil.equals(oldUser.getRoleId(), req.getRoleId())) {
      userToUpdate.setRoleId(req.getRoleId());

      final String roleFullPathToUpdate = Optional.ofNullable(
          userMapper.selectRoleFullPathByRoleId(req.getRoleId())
        )
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到角色数据"));

      if (roleService.isNotSubordinateRole(roleFullPathToUpdate)) {
        throw new ApiException(HttpStatus.FORBIDDEN, "新角色不是当前用户的下级角色");
      }
    }

    // 更新数据库中的用户
    userMapper.update(userToUpdate);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 修改本账号信息。
   *
   * @param req 请求参数
   * @return 204 HTTP 状态码
   */
  public ResponseEntity<Void> updateSelf(final UpdateSelfRequest req) {
    // 获取当前用户的用户 id
    final long userId = AuthUtils.getCurrentUser().orElseThrow().getUserId();

    // 检索数据库，获取旧用户数据
    final UserBaseInfo oldUser = Optional.ofNullable(userMapper.selectBaseById(userId)).orElseThrow();

    // 设置需要更新的字段
    final User userToUpdate = new User();
    userToUpdate.setId(userId);
    userToUpdate.setNickname(req.getNickname());

    // 若需要修改密码，则检验旧密码是否正确
    if (!StrUtil.isBlank(req.getOldPassword()) && !StrUtil.isBlank(req.getNewPassword())) {
      if (!passwordEncoder.matches(req.getOldPassword(), oldUser.getHashedPassword())) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "旧密码错误");
      }

      // 将明文密码进行 Hash 计算后再保存
      final String newHashedPassword = passwordEncoder.encode(req.getNewPassword());
      userToUpdate.setHashedPassword(newHashedPassword);
    }

    // 更新数据库中的用户
    userMapper.update(userToUpdate);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 重置密码。
   *
   * <p>无需验证旧密码。
   *
   * @param id 需要重置密码的用户 id
   * @param req 请求参数
   * @return 204 HTTP 状态码
   */
  public ResponseEntity<Void> resetPassword(final long id, final ResetPasswordRequest req) {
    // 检索数据库，获取旧用户数据
    final UserBaseInfo oldUser = Optional.ofNullable(userMapper.selectBaseById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到用户数据"));

    // 检验是否为当前用户的下级用户
    if (isNotSubordinateRole(oldUser.getRoleId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许更新下级用户");
    }

    // 设置需要更新的字段
    final User userToUpdate = new User();
    userToUpdate.setId(id);

    // 将明文密码进行 Hash 计算后再保存
    final String newHashedPassword = passwordEncoder.encode(req.getPassword());

    userToUpdate.setHashedPassword(newHashedPassword);

    // 更新至数据库
    userMapper.update(userToUpdate);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 删除用户。
   *
   * <p>用户仅可删除其下级角色的用户。
   *
   * @param id 需要删除的用户 id
   * @return 204 HTTP 状态码
   */
  public ResponseEntity<Void> deleteUser(final long id) {
    // 检索数据库，获取旧用户数据
    final UserBaseInfo oldUser = Optional.ofNullable(userMapper.selectBaseById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到用户数据"));

    // 检验是否为当前用户的下级用户
    if (isNotSubordinateRole(oldUser.getRoleId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许删除下级用户");
    }

    // 更新数据库中的用户
    userMapper.deleteById(id);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  private void setFuzzyQueryParams(final GetUserRequest query) {
    query.setUsername(StrUtils.toNullableLikeValue(query.getUsername()));
    query.setNickname(StrUtils.toNullableLikeValue(query.getNickname()));
    query.setRoleName(StrUtils.toNullableLikeValue(query.getRoleName()));
  }

  private void checkNameUniqueness(final String username) {
    final boolean existsUsername = userMapper.existsByUsername(username);

    if (existsUsername) {
      throw new ApiException(HttpStatus.CONFLICT, "已存在相同用户名");
    }
  }

  private boolean isNotSubordinateRole(final long roleId) {
    final String oldRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathByRoleId(roleId)).orElseThrow();

    return roleService.isNotSubordinateRole(oldRoleFullPath);
  }

  private boolean isNotSubordinateRole(final String checkedFullPath) {
    return roleService.isNotSubordinateRole(checkedFullPath);
  }
}
