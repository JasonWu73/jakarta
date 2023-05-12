package net.wuxianjie.springbootweb.role;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.oplog.Log;
import net.wuxianjie.springbootweb.role.dto.AddRoleRequest;
import net.wuxianjie.springbootweb.role.dto.RoleResponse;
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
}
