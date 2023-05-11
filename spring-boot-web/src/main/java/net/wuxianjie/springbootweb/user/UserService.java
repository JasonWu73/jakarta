package net.wuxianjie.springbootweb.user;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.auth.AccountStatus;
import net.wuxianjie.springbootweb.auth.AuthUtils;
import net.wuxianjie.springbootweb.shared.pagination.PaginationParam;
import net.wuxianjie.springbootweb.shared.pagination.PaginationResult;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import net.wuxianjie.springbootweb.shared.util.StringUtils;
import net.wuxianjie.springbootweb.user.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

  /**
   * 获取用户列表。
   *
   * <p>用户仅可查看其下级角色的用户。
   *
   * @param pagination 分页请求参数
   * @param request 请求参数
   * @return 用户分页列表
   */
  public ResponseEntity<PaginationResult<UserResponse>> getUsers(
    final PaginationParam pagination,
    final GetUserRequest request
  ) {
    // 设置模糊查询参数
    request.setUsername(StringUtils.toNullableLikeValue(request.getUsername()));
    request.setNickname(StringUtils.toNullableLikeValue(request.getNickname()));
    request.setRoleName(StringUtils.toNullableLikeValue(request.getRoleName()));

    // 获取当前登录用户的角色完整路径以便查找其下级角色的用户
    final long userId = AuthUtils.getCurrentUser().orElseThrow().userId();
    final String roleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathById(userId)).orElseThrow();
    final String subRoleLikeFullPath = roleFullPath + ".%";

    // 查询数据库获取列表数据
    final List<UserResponse> logs = userMapper.selectByQuery(pagination, request, subRoleLikeFullPath);

    // 查询数据库获取总条目数
    final long total = userMapper.countByQuery(request, subRoleLikeFullPath);

    // 构造分页结果
    return ResponseEntity.ok(new PaginationResult<>(
      pagination.getPageNumber(),
      pagination.getPageSize(),
      total,
      logs
    ));
  }

  /**
   * 新增用户。
   *
   * <p>只允许新增当前用户的下级角色用户。
   *
   * @param request 请求参数
   * @return 201 HTTP 状态码
   */
  public ResponseEntity<Void> addUser(final AddUserRequest request) {
    // 用户名唯一性校验
    final boolean existsUsername = userMapper.existsByUsername(request.getUsername());

    if (existsUsername) {
      throw new ApiException(HttpStatus.CONFLICT, "已存在相同用户名");
    }

    // 判断新增用户的角色是否存在
    final String savedRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathByRoleId(request.getRoleId()))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到新增用户的角色"));

    // 判断新增用户的角色是否为当前用户的下级角色
    if (isNotCurrentUserSubRole(savedRoleFullPath)) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许创建下级角色的用户");
    }

    // 将明文密码进行 Hash 计算后再保存
    final String hashedPassword = passwordEncoder.encode(request.getPassword());

    // 保存至数据库
    final User user = new User();
    user.setUsername(request.getUsername());
    user.setNickname(request.getNickname());
    user.setHashedPassword(hashedPassword);
    user.setStatus(AccountStatus.resolve(request.getStatus()).orElseThrow());
    user.setRoleId(request.getRoleId());
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    user.setRemark(request.getRemark());

    if (userMapper.insert(user) == 0) {
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "新增用户失败");
    }

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * 更新用户。
   *
   * <p>只允许更新当前用户的下级角色用户。
   *
   * @param id 用户 id
   * @param request 请求参数
   * @return 204 HTTP 状态码
   */
  public ResponseEntity<Void> updateUser(final long id, final UpdateUserRequest request) {
    // 判断要更新的用户是否存在
    final User updatedUser = Optional.ofNullable(userMapper.selectById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到要更新的用户"));

    // 判断更新的用户是否为当前用户的下级角色的用户
    final String updatedUserRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathByRoleId(updatedUser.getRoleId()))
      .orElseThrow();

    if (isNotCurrentUserSubRole(updatedUserRoleFullPath)) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许更新下级角色的用户");
    }

    // 判断是否需要更新用户的角色
    if (!Objects.equals(updatedUser.getRoleId(), request.getRoleId())) {
      // 判断更新的目标角色是否存在
      final String newUpdatedRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathByRoleId(request.getRoleId()))
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到更新用户的角色"));

      // 判断更新的目标角色是否为当前用户的下级角色
      if (isNotCurrentUserSubRole(newUpdatedRoleFullPath)) {
        throw new ApiException(HttpStatus.FORBIDDEN, "只允许更新为下级角色的用户");
      }
    }

    // 更新至数据库
    updatedUser.setNickname(request.getNickname());
    updatedUser.setStatus(AccountStatus.resolve(request.getStatus()).orElseThrow());
    updatedUser.setRoleId(request.getRoleId());
    updatedUser.setUpdatedAt(LocalDateTime.now());
    updatedUser.setRemark(request.getRemark());

    if (userMapper.update(updatedUser) == 0) {
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "更新用户失败");
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 修改本账号信息。
   *
   * @param request 请求参数
   * @return 204 HTTP 状态码
   */
  public ResponseEntity<Void> updateSelf(final UpdateSelfRequest request) {
    // 获取当前登录的用户信息
    final long userId = AuthUtils.getCurrentUser().orElseThrow().userId();
    final User user = Optional.ofNullable(userMapper.selectById(userId)).orElseThrow();

    // 更新记录修改时间
    user.setUpdatedAt(LocalDateTime.now());

    // 修改昵称
    user.setNickname(request.getNickname());

    // 判断是否需要修改密码
    if (!StrUtil.isBlank(request.getOldPassword()) && !StrUtil.isBlank(request.getNewPassword())) {
      // 判断旧密码是否正确
      if (!passwordEncoder.matches(request.getOldPassword(), user.getHashedPassword())) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "密码错误");
      }

      // 将明文密码进行 Hash 计算后再保存
      final String newHashedPassword = passwordEncoder.encode(request.getNewPassword());
      user.setHashedPassword(newHashedPassword);
    }

    // 更新至数据库
    if (userMapper.update(user) == 0) {
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "更新账号失败");
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 重置密码。
   *
   * <p>无法旧密码即可重置。
   *
   * @param id 用户 id
   * @param request 请求参数
   * @return 204 HTTP 状态码
   */
  public ResponseEntity<Void> resetPassword(final long id, final ResetPasswordRequest request) {
    // 判断要更新的用户是否存在
    final User user = Optional.ofNullable(userMapper.selectById(id))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "未找到要重置密码的用户"));

    // 判断更新的用户是否为当前用户的下级角色的用户
    final String updatedUserRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathByRoleId(user.getRoleId()))
      .orElseThrow();

    if (isNotCurrentUserSubRole(updatedUserRoleFullPath)) {
      throw new ApiException(HttpStatus.FORBIDDEN, "只允许重置下级角色的用户密码");
    }

    // 更新记录修改时间
    user.setUpdatedAt(LocalDateTime.now());

    // 将明文密码进行 Hash 计算后再保存
    final String newHashedPassword = passwordEncoder.encode(request.getPassword());
    user.setHashedPassword(newHashedPassword);

    // 更新至数据库
    if (userMapper.update(user) == 0) {
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "更新用户失败");
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  private boolean isNotCurrentUserSubRole(final String roleFullPath) {
    final long currentUserId = AuthUtils.getCurrentUser().orElseThrow().userId();

    final String currentRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathById(currentUserId))
      .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "无法获取当前用户的角色信息"));

    return !roleFullPath.startsWith(currentRoleFullPath + "."); // 下级
  }
}
