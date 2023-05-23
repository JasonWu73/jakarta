package net.wuxianjie.springbootweb.test.jpa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 命令行程序。
 *
 * @author 吴仙杰
 */
@Component
public class CommandLineApp {

  @Bean
  public CommandLineRunner commandLineRunner(final String[] args) {
    return runner -> {
      System.out.println("Hello World");
    };
  }
}
