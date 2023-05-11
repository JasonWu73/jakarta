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
}
