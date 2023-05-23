package net.wuxianjie.springbootweb.test;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 测试 Bean 生命周期方法。
 *
 * @author 吴仙杰
 */
@Component
@Slf4j
public class BeanLifecycleMethod {

  /**
   * 在 Bean 于 Spring 容器中完成依赖注入后执行的代码。
   */
  @PostConstruct
  public void doStartup() {
    log.info("在 Bean 于 Spring 容器中完成依赖注入后执行的代码: {}", getClass().getSimpleName());
  }

  /**
   * 在 Bean 要从 Spring 容器中移除前执行的代码。
   */
  @PreDestroy
  public void doCleanUp() {
    log.info("在 Bean 要从 Spring 容器中移除前执行的代码: {}", getClass().getSimpleName());
  }
}
