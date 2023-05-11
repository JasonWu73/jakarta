package net.wuxianjie.springbootweb.user;

import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.shared.pagination.PaginationParam;
import net.wuxianjie.springbootweb.shared.pagination.PaginationResult;
import net.wuxianjie.springbootweb.shared.util.StringUtils;
import net.wuxianjie.springbootweb.user.dto.GetUserRequest;
import net.wuxianjie.springbootweb.user.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户管理相关处理。
 *
 * @author 吴仙杰
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserMapper userMapper;

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
}
