package net.wuxianjie.springbootweb.auth;

import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
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
 * @author 吴仙杰
 **/
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final HandlerExceptionResolver handlerExceptionResolver;
  private final TokenAuth tokenAuth;

  // ========== Spring Security 测试配置 ==========

  /**
   * <p>静态资源需要排除在 Spring Security 之外，否则会导致浏览器无法缓存。
   *
   * <p>因为 Spring Security 会对所有经过其过滤器链的请求设置为不缓存，
   * <br>即在 HTTP 响应头中添加 {@code Cache-Control: no-cache, no-store, max-age=0, must-revalidate}。
   *
   * <p>Spring Boot Web 静态资源查找目录，由优先级高到低排序：
   *
   * <ol>
   *   <li>{@code src/main/resources/META-INF/resources/}</li>
   *   <li>{@code src/main/resources/resources/}</li>
   *   <li>{@code src/main/resources/static/}</li>
   *   <li>{@code src/main/resources/public/}</li>
   * </ol>
   *
   * @return 配置静态资源
   */
//  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers(
      "/images/**",
      "/vendor/**"
    );
  }


//  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
      .anyRequest().authenticated().and()
      .formLogin()
      .loginPage("/thymeleaf/login")
      .loginProcessingUrl("/authenticateTheUser").permitAll().and()
      .logout().permitAll().and()
      .exceptionHandling(c -> c.accessDeniedPage("/thymeleaf/access-denied"));

    return http.build();
  }

//  @Bean
  public DaoAuthenticationProvider authenticationProvider(final UserDetailsService userService) {
    final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

    provider.setUserDetailsService(userService);
    provider.setPasswordEncoder(passwordEncoder());

    return provider;
  }

  // ========== Spring Security 正式配置 ==========

  /**
   * 将 Bcrypt 哈希算法作为 Spring Security 身份验证管理的密码编码器，密码有且仅有 60 位字符。
   *
   * <h2>{@code {id}encodedPassword}</h2>
   *
   * <p>若不注入此 Bean，则使用 Spring Security 默认密码格式：{@code {id}encodedPassword}。
   *
   * <p>常用编码算法 id 有：
   *
   * <ul>
   *   <li>noop：明文密码</li>
   *   <li>bcrypt：BCrypt 哈希密码</li>
   * </ul>
   *
   * @return Bcrypt 密码编码器
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    // 固定使用 BCrypt 作为项目的密码编码器
    return new BCryptPasswordEncoder();

    // 使用 Spring Security 默认的密码编码器
//    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  /**
   * 配置 Spring Security 的过滤器链。
   *
   * <p>若不注入此 Bean，则使用 Spring Security 默认配置。
   *
   * @param http HTTP 安全配置器
   * @return 配置后的过滤器链
   * @throws Exception 当配置失败时抛出
   */
  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    // 以下配置仅对 API 请求（即以 `/api/` 为前缀的 Path）生效
    http
      .securityMatcher("/api/**")
      // 按顺序比较，符合则退出后续比较
      .authorizeHttpRequests()
      // 开放获取鉴权信息相关 API
      .requestMatchers("/api/v1/token/**").permitAll()
      // 开放获取项目版本号 API
      .requestMatchers("/api/v1/version").permitAll()
      // 开放测试相关 API
      .requestMatchers("/api/v1/test/**").permitAll()
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
      // 未通过身份验证 401
      .authenticationEntryPoint((req, resp, exc) -> handlerExceptionResolver.resolveException(
          req, resp, null, new ApiException(HttpStatus.UNAUTHORIZED, "授权失败", exc)))
      // 通过身份验证，但权限不足 403
      .accessDeniedHandler((req, resp, exc) -> handlerExceptionResolver.resolveException(
          req, resp, null, new ApiException(HttpStatus.FORBIDDEN, "权限不足", exc)));

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
}
