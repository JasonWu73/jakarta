package net.wuxianjie.springbootweb.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.oplog.Log;
import net.wuxianjie.springbootweb.shared.pagination.PaginationParam;
import net.wuxianjie.springbootweb.shared.pagination.PaginationResult;
import net.wuxianjie.springbootweb.user.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
   * <p>用户仅可查看其下级角色的用户。
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

  /**
   * 新增用户。
   *
   * <p>只允许新增当前用户的下级角色用户。
   *
   * @param request 请求参数
   * @return 201 HTTP 状态码
   */
  @Log("新增用户")
  @PostMapping("/users")
  @PreAuthorize("hasAuthority('user_add')")
  public ResponseEntity<Void> addUser(@RequestBody @Valid final AddUserRequest request) {
    return userService.addUser(request);
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
  @Log("更新用户")
  @PutMapping("/users/{id}")
  @PreAuthorize("hasAuthority('user_edit')")
  public ResponseEntity<Void> updateUser(
    @PathVariable final long id,
    @RequestBody @Valid final UpdateUserRequest request
  ) {
    return userService.updateUser(id, request);
  }

  /**
   * 修改本账号信息。
   *
   * @param request 请求参数
   * @return 204 HTTP 状态码
   */
  @PutMapping("/self")
  public ResponseEntity<Void> updateSelf(@RequestBody @Valid final UpdateSelfRequest request) {
    return userService.updateSelf(request);
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
  @Log("重置密码")
  @PutMapping("/users/{id}/reset")
  @PreAuthorize("hasAuthority('user_reset')")
  public ResponseEntity<Void> resetPassword(
    @PathVariable final long id,
    @RequestBody @Valid final ResetPasswordRequest request
  ) {
    return userService.resetPassword(id, request);
  }
}
