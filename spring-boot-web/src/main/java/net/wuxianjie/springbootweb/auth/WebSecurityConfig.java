package net.wuxianjie.springbootweb.auth;

import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.sql.DataSource;

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

  // FIXME: 测试完请删除
  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
      .requestMatchers("/").hasRole("EMPLOYEE")
      .requestMatchers("/leaders/**").hasRole("MANAGER")
      .requestMatchers("/systems/**").hasRole("ADMIN")
      .anyRequest().authenticated().and()
      .formLogin()
      .loginPage("/showMyLoginPage")
      .loginProcessingUrl("/authenticateTheUser")
      .permitAll().and()
      .logout().permitAll().and()
      .exceptionHandling(c -> c.accessDeniedPage("/access-denied"));

    return http.build();
  }

  // FIXME: delete me
  @Bean
  public UserDetailsManager userDetailsManager(final DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }

  // FIXME: 测试完请删除
//  @Bean
  public InMemoryUserDetailsManager userDetailsManager() {
    final UserDetails john = User.builder()
      .username("john")
      .password("{noop}111")
      .roles("EMPLOYEE")
      .build();

    final UserDetails mary = User.builder()
      .username("mary")
      .password("{noop}111")
      .roles("EMPLOYEE", "MANAGER")
      .build();

    final UserDetails susan = User.builder()
      .username("susan")
      .password("{noop}111")
      .roles("EMPLOYEE", "MANAGER", "ADMIN")
      .build();

    return new InMemoryUserDetailsManager(john, mary, susan);
  }

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
    // 使用 Spring Security 默认的密码编码器
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    // 固定使用 BCrypt 作为项目的密码编码器
    // FIXME: 测试完成后使用 BCrypt 作为密码编码器
//    return new BCryptPasswordEncoder();
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
//  @Bean
  // FIXME: 测试完后恢复注入
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
