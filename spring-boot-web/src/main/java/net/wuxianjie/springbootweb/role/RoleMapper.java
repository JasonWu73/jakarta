package net.wuxianjie.springbootweb.role;

import net.wuxianjie.springbootweb.role.dto.RoleResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色相关的 SQL 语句。
 *
 * @author 吴仙杰
 */
@Mapper
public interface RoleMapper {

  /**
   * 根据角色 id 获取角色数据。
   *
   * @param id 需要查找的角色 id
   * @return 角色数据
   */
  Role selectById(long id);

  /**
   * 根据用户 id 获取角色数据。
   *
   * @param userId 需要查找的用户 id
   * @return 角色数据
   */
  Role selectByUserId(long userId);

  /**
   * 判断角色名是否存在。
   *
   * @param name 需要查找的角色名
   * @return 角色名是否存在
   */
  boolean existsByName(String name);

  /**
   * 判断是否存在下级角色。
   *
   * @param subRoleLikeFullPath 下级角色的完整路径前缀
   * @return 是否存在下级角色
   */
  boolean existsByFullPathLike(String subRoleLikeFullPath);

  /**
   * 判断是否存在指定角色的用户。
   *
   * @param id 需要查找的角色 id
   * @return 是否存指定角色的用户
   */
  boolean existsUserById(long id);

  /**
   * 获取所有下级角色。
   *
   * @param subRoleLikeFullPath 下级角色的完整路径前缀
   * @return 所有下级角色
   */
  List<RoleResponse> selectAllByFullPathLikeOrderByUpdatedAt(String subRoleLikeFullPath);

  /**
   * 新增角色。
   *
   * @param role 需要新增的角色数据
   * @return 新增记录数
   */
  int insert(Role role);

  /**
   * 更新角色。
   *
   * @param role 需要更新的角色数据
   * @return 更新记录数
   */
  int update(Role role);

  /**
   * 更新下级的父角色名。
   *
   * @param name 父角色名
   * @param id 父角色 id
   * @return 更新记录数
   */
  int updateParentNameByParentId(String name, long id);

  /**
   * 更新所有下节角色的完整路径。
   *
   * @param newFullPath 新的完整路径，以 {@code .} 结尾
   * @param oldFullPath 旧的完整路径，以 {@code .} 结尾
   * @return 更新记录数
   */
  int updateSubFullPath(String newFullPath, String oldFullPath);

  /**
   * 删除角色。
   *
   * @param id 需要删除的角色 id
   * @return 删除记录数
   */
  int deleteById(long id);
}
