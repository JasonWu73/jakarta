package net.wuxianjie.springbootweb.shared.security;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Token 缓存配置。
 *
 * @author 吴仙杰
 */
@Configuration
@RequiredArgsConstructor
public class TokenCacheConfig {

  private final SecurityProps securityProps;

  /**
   * 用于 Token 身份验证的定时缓存。
   *
   * @return Token 登录缓存（{@code {username: AuthData}}）
   */
  @Bean
  public TimedCache<String, AuthData> tokenCache() {
    // 创建 Token 定时缓存
    final int timeoutMs = securityProps.getExpirationSec() * 1000;
    final TimedCache<String, AuthData> cache = CacheUtil.newTimedCache(timeoutMs);

    // 启动定时任务，每到过期时间时清理一次过期条目
    cache.schedulePrune(timeoutMs);

    return cache;
  }
}
