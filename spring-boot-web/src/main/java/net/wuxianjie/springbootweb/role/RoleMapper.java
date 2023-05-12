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
   * 获取所有下级角色。
   *
   * @param subRoleLikeFullPath 下级角色的完整路径前缀
   * @return 所有下级角色
   */
  List<RoleResponse> selectAllByFullPathLike(String subRoleLikeFullPath);
}
