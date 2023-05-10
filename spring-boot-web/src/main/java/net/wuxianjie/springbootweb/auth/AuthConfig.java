package net.wuxianjie.springbootweb.auth;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import net.wuxianjie.springbootweb.auth.dto.AuthData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

/**
 * 授权管理相关配置。
 *
 * @author 吴仙杰
 */
@Configuration
public class AuthConfig {

  /**
   * 用于 Token 身份验证的定时缓存。
   *
   * @return Token 登录缓存（{@code {username: AuthData}}）
   */
  @Bean
  public TimedCache<String, AuthData> accessTokenCache() {
    // 创建 Token 定时缓存
    final int timeoutMs = AuthProps.TOKEN_EXPIRATION_SEC * 1000;
    final TimedCache<String, AuthData> cache = CacheUtil.newTimedCache(timeoutMs);

    // 启动定时任务，每到过期时间时清理一次过期条目
    cache.schedulePrune(timeoutMs);

    return cache;
  }

  /**
   * 配置拥有上下级关系的功能权限。
   *
   * <p>Spring Boot 3 即 Spring Security 6 开始，还需要创建 {@link #expressionHandler()}，
   * <br>否则无法在方法之上使用 {@code @PreAuthorize("hasAuthority('user_add')")}。
   *
   * @return 拥有上下级关系的功能权限
   */
  @Bean
  public RoleHierarchy roleHierarchy() {
    final RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy(Authority.getHierarchy());
    return roleHierarchy;
  }

  /**
   * 结合 {@link #roleHierarchy()} 使用。
   *
   * @return 实现拥有上下级关系的功能权限
   */
  @Bean
  public DefaultMethodSecurityExpressionHandler expressionHandler() {
    final DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    expressionHandler.setRoleHierarchy(roleHierarchy());
    return expressionHandler;
  }
}
