package net.wuxianjie.springbootweb.auth;

import net.wuxianjie.springbootweb.auth.dto.AuthData;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * 身份验证工具类。
 *
 * @author 吴仙杰
 */
public class AuthUtils {

  /**
   * 获取当前登录用户数据。
   *
   * @return 当前登录用户数据
   */
  public static Optional<AuthData> getCurrentUser() {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || auth instanceof AnonymousAuthenticationToken) {
      return Optional.empty();
    }

    return Optional.of((AuthData) auth.getPrincipal());
  }
}
