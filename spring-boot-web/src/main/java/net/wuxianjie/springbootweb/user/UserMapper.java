package net.wuxianjie.springbootweb.user;

import net.wuxianjie.springbootweb.shared.pagination.PaginationParam;
import net.wuxianjie.springbootweb.user.dto.GetUserRequest;
import net.wuxianjie.springbootweb.user.dto.UserBaseInfo;
import net.wuxianjie.springbootweb.user.dto.UserDetailResponse;
import net.wuxianjie.springbootweb.user.dto.UserItemResponse;
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

  UserBaseInfo selectBaseById(long userId);

  UserDetailResponse selectUserDetailById(long userId);

  String selectRoleFullPathByRoleId(long roleId);

  String selectRoleFullPathById(long userId);

  boolean existsByUsername(String username);

  List<UserItemResponse> selectByQueryOrderByUpdatedAtDescLimit(
    @Param("p") PaginationParam pag,
    @Param("q") GetUserRequest query,
    @Param("fullPath") String fullPath
  );

  long countByQuery(
    @Param("q") GetUserRequest query,
    @Param("fullPath") String fullPath
  );

  int insert(User user);

  int update(User user);

  int deleteById(long id);
}
