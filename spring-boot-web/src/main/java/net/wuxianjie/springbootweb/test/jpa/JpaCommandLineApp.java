package net.wuxianjie.springbootweb.test.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * JPA 命令行测试程序。
 *
 * @author 吴仙杰
 */
@Component
@Slf4j
public class JpaCommandLineApp {

  @Bean
  public CommandLineRunner commandLineRunner(final String[] args) {
    return runner -> {
      log.info("Hello World");
    };
  }
}
