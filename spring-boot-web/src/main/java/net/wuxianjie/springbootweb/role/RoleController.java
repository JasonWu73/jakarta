package net.wuxianjie.springbootweb.role;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.oplog.Log;
import net.wuxianjie.springbootweb.role.dto.AddRoleRequest;
import net.wuxianjie.springbootweb.role.dto.RoleResponse;
import net.wuxianjie.springbootweb.role.dto.UpdateRoleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理相关 REST API。
 *
 * @author 吴仙杰
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RoleController {

  private final RoleService roleService;

  /**
   * 获取角色列表。
   *
   * <p>用户仅可查看其下级角色。
   *
   * @return 角色列表
   */
  @GetMapping("/roles")
  @PreAuthorize("hasAuthority('role_view')")
  public ResponseEntity<List<RoleResponse>> getRoles() {
    return roleService.getRoles();
  }

  /**
   * 新增角色。
   *
   * <p>只允许新增当前用户的下级角色。
   *
   * @param request 请求参数
   * @return 201 HTTP 状态码
   */
  @Log("新增角色")
  @PostMapping("/roles")
  @PreAuthorize("hasAuthority('role_add')")
  public ResponseEntity<Void> addRole(@RequestBody @Valid final AddRoleRequest request) {
    return roleService.addRole(request);
  }

  /**
   * 更新角色。
   *
   * <p>只允许更新当前用户的下级角色。
   *
   * @param id 需要更新的角色 id
   * @param request 请求参数
   * @return 204 HTTP 状态码
   */
  @Log("更新角色")
  @PutMapping("/roles/{id}")
  @PreAuthorize("hasAuthority('role_edit')")
  public ResponseEntity<Void> updateRole(
    @PathVariable final long id,
    @RequestBody @Valid final UpdateRoleRequest request
  ) {
    return roleService.updateRole(id, request);
  }

  /**
   * 删除角色。
   *
   * <p>只允许更新当前用户的下级角色。
   *
   * @param id 需要删除的角色 id
   * @return 204 HTTP 状态码
   */
  @Log("删除角色")
  @DeleteMapping("/roles/{id}")
  @PreAuthorize("hasAuthority('role_del')")
  public ResponseEntity<Void> deleteRole(@PathVariable final long id) {
    return roleService.deleteRole(id);
  }
}
