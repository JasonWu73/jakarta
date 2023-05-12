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
public enum Authority {

  // 切记：每次添加节点时，都需要手动修改 `getRoleHierarchy` 方法

  /**
   * 定义权限树中的根节点，即系统最高权限。
   */
  ROOT("1", "0", "根权限", "root"),

  // 用户管理
  USER(ROOT.id + ".1", ROOT.id, "用户管理", "user"),
  USER_VIEW(USER.id + ".1", USER.id, "查看用户", "user_view"),
  USER_ADD(USER.id + ".2", USER.id, "新增用户", "user_add"),
  USER_EDIT(USER.id + ".3", USER.id, "编辑用户", "user_edit"),
  USER_DEL(USER.id + ".4", USER.id, "删除用户", "user_del"),
  USER_RESET(USER.id + ".5", USER.id, "重置密码", "user_reset"),

  // 角色管理
  ROLE(ROOT.id + ".2", ROOT.id, "角色管理", "role"),
  ROLE_VIEW(ROLE.id + ".1", ROLE.id, "查看角色", "role_view"),
  ROLE_ADD(ROLE.id + ".2", ROLE.id, "新增角色", "role_add"),
  ROLE_EDIT(ROLE.id + ".3", ROLE.id, "编辑角色", "role_edit"),
  ROLE_DEL(ROLE.id + ".4", ROLE.id, "删除角色", "role_del"),

  // 操作日志
  OP_LOG(ROOT.id + ".3", ROOT.id, "操作日志", "op_log"),
  OP_LOG_VIEW(OP_LOG.id + ".1", OP_LOG.id, "查看日志", "op_log_view");

  /**
   * 获取符合 {@link RoleHierarchyImpl} 的权限字符串。
   *
   * @return 符合 {@link RoleHierarchyImpl} 的权限字符串
   */
  public static String getHierarchy() {
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
      ROLE.code + " > " + ROLE_DEL.code + "\n" +
      ROOT.code + " > " + OP_LOG.code + "\n" +
      OP_LOG.code + " > " + OP_LOG_VIEW.code + "\n";
  }

  /**
   * 权限 id。
   */
  private final String id;

  /**
   * 父权限 id。
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
   * 获取完整权限树列表。
   *
   * @return 包含全部数据的权限树列表
   */
  public static Tree<String> getTree() {
    final List<TreeNode<String>> nodes = Arrays.stream(values())
      .map(auth ->
        new TreeNode<>(auth.id, auth.parentId, auth.name, 1)
          .setExtra(Map.of("code", auth.code))
      )
      .toList();

    return TreeUtil.build(nodes, ROOT.parentId).get(0);
  }

  /**
   * 检查节点是否为下级节点，即判断是否拥有指定权限。
   *
   * @param parentCode 比较的父节点 code
   * @param checkedCode 被检查的下级节点 code
   * @return {@code checkedCode} 是否为 {@code parentCode} 的下级节点
   * @throws IllegalArgumentException 当 code 解析失败时
   */
  public static boolean isSubNode(
    final String parentCode,
    final String checkedCode
  ) throws IllegalArgumentException {
    final String parentId = resolve(parentCode)
      .map(Authority::getId)
      .orElseThrow(() -> new IllegalArgumentException("无法识别 parentCode: " + parentCode));

    final String checkedId = resolve(checkedCode)
      .map(Authority::getId)
      .orElseThrow(() -> new IllegalArgumentException("无法识别 checkedCode: " + checkedCode));

    return checkedId.startsWith(parentId + "."); // 下级
  }

  /**
   * 解析枚举值。
   *
   * @param code 需要解析的权限代码
   * @return 权限枚举值
   */
  public static Optional<Authority> resolve(final String code) {
    try {
      // 定义时保证常量名与 `code` 保持一致，故可采用 `valueOf()` 方法解析
      return Optional.of(valueOf(code.toUpperCase()));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}
