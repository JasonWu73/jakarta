package net.wuxianjie.springbootweb.role;

import net.wuxianjie.springbootweb.role.dto.RoleBaseInfo;
import net.wuxianjie.springbootweb.role.dto.RoleItemResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色所需的 SQL 语句。
 *
 * @author 吴仙杰
 */
@Mapper
public interface RoleMapper {

  RoleBaseInfo selectBaseById(long roleId);

  RoleBaseInfo selectBaseByUserId(long userId);

  boolean existsRoleByName(String name);

  boolean existsRoleByFullPathLike(String fullPath);

  boolean existsUserByRoleId(long roleId);

  List<RoleItemResponse> selectByFullPathLikeOrderByUpdatedAtDesc(String fullPath);

  int insert(Role role);

  int update(Role role);

  int updateParentNameByParentId(
    @Param("parentName") String parentName,
    @Param("parentId") long parentId
  );

  int updateFullPathByFullPathLike(
    @Param("newFullPath") String newFullPath,
    @Param("oldFullPath") String oldFullPath
  );

  int deleteById(long roleId);
}
