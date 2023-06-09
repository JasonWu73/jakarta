package net.wuxianjie.springbootweb.user;

import net.wuxianjie.springbootweb.oplog.Log;
import net.wuxianjie.springbootweb.shared.pagination.PaginationParam;
import net.wuxianjie.springbootweb.shared.pagination.PaginationResult;
import net.wuxianjie.springbootweb.user.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理 REST API。
 *
 * @author 吴仙杰
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   * 获取用户分页列表。
   *
   * <p>用户仅可查看其下级角色的用户。
   *
   * @param pag 分页参数
   * @param query 查询参数
   * @return 用户分页列表
   */
  @GetMapping("/users")
  @PreAuthorize("hasAuthority('user_view')")
  public ResponseEntity<PaginationResult<UserItemResponse>> getUsers(
    @Valid final PaginationParam pag,
    @Valid final GetUserRequest query
  ) {
    return userService.getUsers(pag, query);
  }

  /**
   * 获取用户详情。
   *
   * <p>用户仅可查看其下级角色的用户。
   *
   * @param userId 需要查找的用户 id
   * @return 用户详情数据
   */
  @GetMapping("/users/{userId}")
  @PreAuthorize("hasAuthority('user_view')")
  public ResponseEntity<UserDetailResponse> getUserDetail(@PathVariable final long userId) {
    return userService.getUserDetail(userId);
  }

  /**
   * 新增用户。
   *
   * <p>用户仅可创建其下级角色的用户。
   *
   * @param req 请求参数
   * @return 201 HTTP 状态码
   */
  @Log("新增用户")
  @PostMapping("/users")
  @PreAuthorize("hasAuthority('user_add')")
  public ResponseEntity<Void> addUser(@RequestBody @Valid final AddUserRequest req) {
    return userService.addUser(req);
  }

  /**
   * 更新用户。
   *
   * <p>用户仅可更新其下级角色的用户。
   *
   * @param userId 需要更新的用户 id
   * @param req 请求参数
   * @return 204 HTTP 状态码
   */
  @Log("更新用户")
  @PutMapping("/users/{userId}")
  @PreAuthorize("hasAuthority('user_edit')")
  public ResponseEntity<Void> updateUser(
    @PathVariable final long userId,
    @RequestBody @Valid final UpdateUserRequest req
  ) {
    return userService.updateUser(userId, req);
  }

  /**
   * 修改本账号信息。
   *
   * @param req 请求参数
   * @return 204 HTTP 状态码
   */
  @PutMapping("/self")
  public ResponseEntity<Void> updateSelf(@RequestBody @Valid final UpdateSelfRequest req) {
    return userService.updateSelf(req);
  }

  /**
   * 重置密码。
   *
   * <p>无需验证旧密码。
   *
   * @param userId 需要重置密码的用户 id
   * @param req 请求参数
   * @return 204 HTTP 状态码
   */
  @Log("重置密码")
  @PutMapping("/users/{userId}/reset")
  @PreAuthorize("hasAuthority('user_reset')")
  public ResponseEntity<Void> resetPassword(
    @PathVariable final long userId,
    @RequestBody @Valid final ResetPasswordRequest req
  ) {
    return userService.resetPassword(userId, req);
  }

  /**
   * 删除用户。
   *
   * <p>用户仅可删除其下级角色的用户。
   *
   * @param userId 需要删除的用户 id
   * @return 204 HTTP 状态码
   */
  @Log("删除用户")
  @DeleteMapping("/users/{userId}")
  @PreAuthorize("hasAuthority('user_del')")
  public ResponseEntity<Void> deleteUser(@PathVariable final long userId) {
    return userService.deleteUser(userId);
  }
}
