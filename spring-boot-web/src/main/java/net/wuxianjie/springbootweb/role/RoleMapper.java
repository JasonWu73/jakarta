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
   * 判断角色名是否存在。
   *
   * @param name 需要查找的角色名
   * @return 角色名是否存在
   */
  boolean existsByName(String name);

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
}
