package net.wuxianjie.springbootweb.role;

import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.role.dto.RoleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
