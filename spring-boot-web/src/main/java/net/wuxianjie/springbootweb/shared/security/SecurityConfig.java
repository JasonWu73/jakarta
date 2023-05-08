package net.wuxianjie.springbootweb.shared.security;

import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Spring Security 配置。
 *
 * <p>业务程序还可参考以下示例代码定义拥有上下级层级关系的功能权限：
 *
 * <pre>{@code
 *   @Bean
 *   public RoleHierarchy roleHierarchy() {
 *     final RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
 *     final String roleHierarchyStr = """
 *       root > menu
 *       root > user
 *       menu > menu_view
 *       menu > menu_add
 *       menu > menu_edit
 *       menu > menu_del
 *       user > user_view
 *       user > user_add
 *       user > user_edit
 *       user > user_del
 *       """;
 *     roleHierarchy.setHierarchy(roleHierarchyStr);
 *     return roleHierarchy;
 *   }
 * }</pre>
 * <p>
 * Controller 使用示例：
 *
 * <pre>{@code
 *   @RestController
 *   @RequestMapping("/api/v1")
 *   public class SecurityTestController {
 *
 *     @GetMapping("/menu")
 *     @PreAuthorize("hasAuthority('menu_view')")
 *     public String getMenu() {
 *       return "menu data";
 *     }
 *   }
 * }</pre>
 *
 * @author 吴仙杰
 **/
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final HandlerExceptionResolver handlerExceptionResolver;
  private final TokenAuth tokenAuth;

  /**
   * <p>静态资源需要排除在 Spring Security 之外，否则会导致浏览器无法缓存。
   *
   * <p>因为 Spring Security 会对所有经过其过滤器链的请求设置为不缓存，
   * <br>即在 HTTP 响应头中添加 {@code Cache-Control: no-cache, no-store, max-age=0, must-revalidate}。
   *
   * @return 配置静态资源
   */
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers(
      "favicon.ico",
      "/UEditor/**",
      "/js/**",
      "/css/**",
      "/assets/**"
    );
  }

  /**
   * 配置 Spring Security 的过滤器链。
   *
   * @param http {@link HttpSecurity}
   * @return 配置后的过滤器链
   * @throws Exception 当配置失败时抛出
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // 以下配置仅对 API 请求生效
    http
      .securityMatcher("/api/**")
      // 按顺序比较，符合则退出后续比较
      .authorizeHttpRequests()
      // 默认所有 API 都需要登录才能访问
      .requestMatchers("/**").authenticated().and()
      // 在进入 Spring Security 身份验证过滤器前添加自定义的 Token 身份验证过滤器
      // 注意：哪怕是 `permitAll` 的 URI 也会进入过滤器
      .addFilterBefore(
        new TokenAuthFilter(handlerExceptionResolver, tokenAuth),
        UsernamePasswordAuthenticationFilter.class
      );

    // 以下配置对所有请求生效
    http
      // 按顺序比较，符合则退出后续比较
      .authorizeHttpRequests()
      // 默认所有请求所有人都可访问（保证 SPA 前端资源可用）
      .requestMatchers("/**").permitAll().and()
      // 允许前端使用 iFrame
      .headers().frameOptions().disable().and()
      // 启用 CORS 并禁用 CSRF
      .cors().and().csrf().disable()
      // 无状态会话，即不向客户端发送 `JSESSIONID` Cookies
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      // 处理 401 及 403 HTTP 状态码
      .exceptionHandling()
      .authenticationEntryPoint((request, response, authException) ->
        handlerExceptionResolver.resolveException(
          request,
          response,
          null,
          new ApiException(
            HttpStatus.UNAUTHORIZED,
            "授权失败",
            authException
          )
        )
      )
      .accessDeniedHandler((request, response, accessDeniedException) ->
        handlerExceptionResolver.resolveException(
          request,
          response,
          null,
          new ApiException(
            HttpStatus.FORBIDDEN,
            "权限不足",
            accessDeniedException
          )
        )
      );

    return http.build();
  }

  /**
   * CORS 过滤器，在 Spring Security 中启用 CORS ({@code http.cors()}) 后生效。
   *
   * @return CORS 过滤器
   */
  @Bean
  public CorsFilter corsFilter() {
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOriginPattern("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return new CorsFilter(source);
  }

  /**
   * 将 Bcrypt 密码哈希算法作为 Spring Security 身份验证管理的密码编码器。
   *
   * @return Bcrypt 密码编码器
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
