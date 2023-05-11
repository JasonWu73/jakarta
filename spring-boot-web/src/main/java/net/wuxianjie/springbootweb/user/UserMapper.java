package net.wuxianjie.springbootweb.user;

import net.wuxianjie.springbootweb.shared.pagination.PaginationParam;
import net.wuxianjie.springbootweb.user.dto.GetUserRequest;
import net.wuxianjie.springbootweb.user.dto.UserResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户相关的 SQL 语句。
 *
 * @author 吴仙杰
 */
@Mapper
public interface UserMapper {

  /**
   * 根据用户 id 获取用户数据。
   *
   * @param id 需要查找的用户 id
   * @return 用户数据
   */
  User selectById(long id);

  /**
   * 通过角色 id 获取角色的完整路径。
   *
   * @param roleId 需要查找的角色 id
   * @return 角色的完整路径
   */
  String selectRoleFullPathByRoleId(long roleId);

  /**
   * 通过用户 id 获取角色的完整路径。
   *
   * @param id 需要查找的用户 id
   * @return 角色的完整路径
   */
  String selectRoleFullPathById(long id);

  /**
   * 判断用户名是否存在。
   *
   * @param username 需要查找的用户名
   * @return 用户名是否存在
   */
  boolean existsByUsername(String username);

  /**
   * 根据查询条件获取用户分页列表。
   *
   * @param pagination 查询分页参数
   * @param request 查询参数
   * @return 过滤后的用户分页列表
   */
  List<UserResponse> selectByQuery(
    @Param("p") PaginationParam pagination,
    @Param("q") GetUserRequest request);

  /**
   * 根据查询条件获取用户总条数。
   *
   * @param request 查询参数
   * @return 过滤后的总条数
   */
  long countByQuery(@Param("q") final GetUserRequest request);

  /**
   * 新增用户。
   *
   * @param user 需要新增的用户数据
   * @return 新增记录数
   */
  int insert(User user);

  /**
   * 更新用户。
   *
   * @param user 需要更新的用户数据
   * @return 更新记录数
   */
  int update(User user);
}
