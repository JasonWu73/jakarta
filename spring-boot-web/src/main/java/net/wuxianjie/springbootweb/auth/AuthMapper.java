package net.wuxianjie.springbootweb.auth;

import net.wuxianjie.springbootweb.auth.dto.RawAuthData;
import org.apache.ibatis.annotations.Mapper;

/**
 * 授权相关的 SQL 语句。
 *
 * @author 吴仙杰
 */
@Mapper
public interface AuthMapper {

  /**
   * 获取身份验证所需数据。
   *
   * @param username 需要查找的用户名
   * @return 身份验证数据
   */
  RawAuthData selectByUsername(String username);
}
