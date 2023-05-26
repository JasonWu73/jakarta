package net.wuxianjie.springbootweb.auth;

import net.wuxianjie.springbootweb.auth.dto.AuthData;
import org.apache.ibatis.annotations.Mapper;

/**
 * 鉴权所需的 SQL 语句。
 *
 * @author 吴仙杰
 */
@Mapper
public interface AuthMapper {

  AuthData selectByUsername(String username);
}
