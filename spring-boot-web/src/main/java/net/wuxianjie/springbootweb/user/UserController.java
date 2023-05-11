package net.wuxianjie.springbootweb.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.shared.pagination.PaginationParam;
import net.wuxianjie.springbootweb.shared.pagination.PaginationResult;
import net.wuxianjie.springbootweb.user.dto.GetUserRequest;
import net.wuxianjie.springbootweb.user.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理相关 REST API。
 *
 * @author 吴仙杰
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   * 获取用户列表。
   *
   * @param pagination 分页请求参数
   * @param request 请求参数
   * @return 用户分页列表
   */
  @GetMapping("/users")
  @PreAuthorize("hasAuthority('user_view')")
  public ResponseEntity<PaginationResult<UserResponse>> getUsers(
    @Valid final PaginationParam pagination,
    @Valid final GetUserRequest request
  ) {
    return userService.getUsers(pagination, request);
  }
}
