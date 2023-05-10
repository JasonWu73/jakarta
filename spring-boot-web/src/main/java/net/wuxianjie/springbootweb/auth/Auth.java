package net.wuxianjie.springbootweb.auth;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 系统所包含的权限值。
 *
 * <p>常用权限主要有以下几类：
 *
 * <ul>
 *   <li>view：查看</li>
 *   <li>add：新增</li>
 *   <li>edit：编辑</li>
 *   <li>del：删除</li>
 * </ul>
 *
 * @author 吴仙杰
 */
@Getter
@ToString
@RequiredArgsConstructor
public enum Auth {

  // 切记：每次添加节点时，都需要手动修改 `getRoleHierarchy` 方法

  /**
   * 定义权限树中的根节点，即系统最高权限。
   */
  ROOT("1", "0", "根权限", "root", "hasAuthority('root')"),

  // 用户管理
  USER(ROOT.id + ".1", ROOT.id, "用户管理", "user", "hasAuthority('user')"),
  USER_VIEW(USER.id + ".1", USER.id, "查看", "user_view", "hasAuthority('user_view')"),
  USER_ADD(USER.id + ".2", USER.id, "新增", "user_add", "hasAuthority('user_add')"),
  USER_EDIT(USER.id + ".3", USER.id, "编辑", "user_edit", "hasAuthority('user_edit')"),
  USER_DEL(USER.id + ".4", USER.id, "删除", "user_del", "hasAuthority('user_del')"),
  USER_RESET(USER.id + ".5", USER.id, "重置密码", "user_reset", "hasAuthority('user_reset')"),

  // 角色管理
  ROLE(ROOT.id + ".2", ROOT.id, "角色管理", "role", "hasAuthority('role')"),
  ROLE_VIEW(ROLE.id + ".1", ROLE.id, "查看", "role_view", "hasAuthority('role_view')"),
  ROLE_ADD(ROLE.id + ".2", ROLE.id, "新增", "role_add", "hasAuthority('role_add')"),
  ROLE_EDIT(ROLE.id + ".3", ROLE.id, "编辑", "role_edit", "hasAuthority('role_edit')"),
  ROLE_DEL(ROLE.id + ".4", ROLE.id, "删除", "role_del", "hasAuthority('role_del')");

  /**
   * 权限 id。
   */
  private final String id;

  /**
   * 上级权限 id。
   */
  private final String parentId;

  /**
   * 权限名称，用于展示说明。
   */
  private final String name;

  /**
   * 权限代码，用于前后端代码中的权限匹配依据。
   */
  private final String code;

  /**
   * Spring Security 权限控制代码。
   *
   * <p>比如在方法之上使用 {@code @PreAuthorize(Auth.ROOT.authorize)}。
   */
  private final String authorize;

  /**
   * 获取完整权限树列表。
   *
   * @return 包含全部数据的权限树列表
   */
  public static List<Tree<String>> getTrees() {
    final List<TreeNode<String>> nodes = Arrays.stream(values())
      .map(auth ->
        new TreeNode<>(auth.id, auth.parentId, auth.name, 1)
          .setExtra(Map.of("code", auth.code))
      )
      .toList();

    return TreeUtil.build(nodes, ROOT.parentId);
  }

  /**
   * 获取符合 {@link RoleHierarchyImpl} 的权限字符串。
   *
   * @return 符合 {@link RoleHierarchyImpl} 的权限字符串
   */
  public static String getAuthHierarchy() {
    return ROOT.code + " > " + USER.code + "\n" +
      USER.code + " > " + USER_VIEW.code + "\n" +
      USER.code + " > " + USER_ADD.code + "\n" +
      USER.code + " > " + USER_EDIT.code + "\n" +
      USER.code + " > " + USER_DEL.code + "\n" +
      USER.code + " > " + USER_RESET.code + "\n" +
      ROOT.code + " > " + ROLE.code + "\n" +
      ROLE.code + " > " + ROLE_VIEW.code + "\n" +
      ROLE.code + " > " + ROLE_ADD.code + "\n" +
      ROLE.code + " > " + ROLE_EDIT.code + "\n" +
      ROLE.code + " > " + ROLE_DEL.code + "\n";
  }

  /**
   * 检查节点是否为本级或下级节点，即判断是否拥有指定权限。
   *
   * @param parentCode 比较的上级节点 code
   * @param checkedCode 被检查的下级节点 code
   * @return {@code checkedCode} 是否为 {@code parentCode} 的本级或下级节点
   * @throws IllegalArgumentException 当 code 解析失败时
   */
  public static boolean isSubNode(
    final String parentCode,
    final String checkedCode
  ) throws IllegalArgumentException {
    final String parentId = resolve(parentCode)
      .map(Auth::getId)
      .orElseThrow(() -> new IllegalArgumentException("无法识别 parentCode: " + parentCode));

    final String checkedId = resolve(checkedCode)
      .map(Auth::getId)
      .orElseThrow(() -> new IllegalArgumentException("无法识别 checkedCode: " + checkedCode));

    return checkedId.equals(parentId) || // 本级
      checkedId.startsWith(parentId + "."); // 下级
  }

  /**
   * 解析枚举值。
   *
   * @param code 需要解析的权限代码
   * @return 权限枚举值
   */
  public static Optional<Auth> resolve(final String code) {
    try {
      // 定义时保证常量名与 `code` 保持一致，故可采用 `valueOf()` 方法解析
      return Optional.of(valueOf(code.toUpperCase()));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}
