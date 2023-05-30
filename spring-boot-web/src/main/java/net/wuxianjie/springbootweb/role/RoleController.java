package net.wuxianjie.springbootweb.role;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.oplog.Log;
import net.wuxianjie.springbootweb.role.dto.AddRoleRequest;
import net.wuxianjie.springbootweb.role.dto.RoleBaseInfo;
import net.wuxianjie.springbootweb.role.dto.RoleItemResponse;
import net.wuxianjie.springbootweb.role.dto.UpdateRoleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理 REST API。
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
   * <p>用户仅可查看自己及其下级角色。
   *
   * @return 角色列表
   */
  @GetMapping("/roles")
  @PreAuthorize("hasAuthority('role_view')")
  public ResponseEntity<List<RoleItemResponse>> getRoles() {
    return roleService.getRoles();
  }

  /**
   * 获取角色详情。
   *
   * <p>用户仅可查看其下级角色。
   *
   * @param id 需要查找的角色 id
   * @return 角色详情数据
   */
  @GetMapping("/roles/{id}")
  @PreAuthorize("hasAuthority('role_view')")
  public ResponseEntity<RoleBaseInfo> getRoleDetail(@PathVariable final long id) {
    return roleService.getRoleDetail(id);
  }

  /**
   * 新增角色。
   *
   * <p>用户仅可创建其下级角色。
   *
   * @param req 请求参数
   * @return 201 HTTP 状态码
   */
  @Log("新增角色")
  @PostMapping("/roles")
  @PreAuthorize("hasAuthority('role_add')")
  public ResponseEntity<Void> addRole(@RequestBody @Valid final AddRoleRequest req) {
    return roleService.addRole(req);
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
  @Log("更新角色")
  @PutMapping("/roles/{id}")
  @PreAuthorize("hasAuthority('role_edit')")
  public ResponseEntity<Void> updateRole(
    @PathVariable final long id,
    @RequestBody @Valid final UpdateRoleRequest req
  ) {
    return roleService.updateRole(id, req);
  }

  /**
   * 删除角色。
   *
   * <p>用户仅可删除其下级角色。
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
