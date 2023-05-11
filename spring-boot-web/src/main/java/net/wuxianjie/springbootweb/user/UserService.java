package net.wuxianjie.springbootweb.user;

import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.auth.AccountStatus;
import net.wuxianjie.springbootweb.auth.AuthUtils;
import net.wuxianjie.springbootweb.shared.pagination.PaginationParam;
import net.wuxianjie.springbootweb.shared.pagination.PaginationResult;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import net.wuxianjie.springbootweb.shared.util.StringUtils;
import net.wuxianjie.springbootweb.user.dto.AddUserRequest;
import net.wuxianjie.springbootweb.user.dto.GetUserRequest;
import net.wuxianjie.springbootweb.user.dto.UserResponse;
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

  /**
   * 获取用户列表。
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

    // 查询数据库获取列表数据
    final List<UserResponse> logs = userMapper.selectByQuery(pagination, request);

    // 查询数据库获取总条目数
    final long total = userMapper.countByQuery(request);

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
   * @param request 请求参数
   * @return 201 HTTP 状态码
   */
  public ResponseEntity<PaginationResult<UserResponse>> addUser(final AddUserRequest request) {
    // 用户名唯一性校验
    final boolean existsUsername = userMapper.existsByUsername(request.getUsername());

    if (existsUsername) {
      throw new ApiException(HttpStatus.CONFLICT, "已存在相同用户名");
    }

    // 判断新增用户的角色是否存在
    final String savedRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathByRoleId(request.getRoleId()))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "用户角色不存在"));

    // 判断新增用户的角色是否为当前用户角色或其下级角色
    final long currentUserId = AuthUtils.getCurrentUser().orElseThrow().userId();

    final String currentRoleFullPath = Optional.ofNullable(userMapper.selectRoleFullPathById(currentUserId))
      .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "无法获取当前用户的角色信息"));

    if (
      !savedRoleFullPath.equals(currentRoleFullPath) && // 本级
        !savedRoleFullPath.startsWith(currentUserId + ".") // 下级
    ) {
      throw new ApiException(HttpStatus.FORBIDDEN, "无法创建上级角色的用户");
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

    if (userMapper.insert(user) != 1) {
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "新增用户失败");
    }

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
