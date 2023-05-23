package net.wuxianjie.springbootweb.test;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用于测试的配置。
 *
 * @author 吴仙杰
 */
@Configuration
public class TestConfig {

  /**
   * 模拟将非 Spring 实现的三方库 SDK 对象注入到 Spring 容器中。
   *
   * @return 模拟的三方库 SDK
   */
  @Bean
  public ThirdLibrarySdk thirdLibrary() {
    return new ThirdLibrarySdk("123.321");
  }

  /**
   * 注入两个同类型的 Bean，使用时需要 Qualified。
   *
   * @return 模拟的另一个三方库 SDK
   */
  @Bean
  @Qualifier("sdk2")
  public ThirdLibrarySdk anotherThirdLibrary() {
    return new ThirdLibrarySdk("456.654");
  }
}
