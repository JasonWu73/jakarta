package net.wuxianjie.springbootweb.test.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * JPA 命令行测试程序。
 *
 * @author 吴仙杰
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JpaCommandLineApp {

  private final UserDao userDao;
  private final PasswordEncoder passwordEncoder;

  @Bean
  public CommandLineRunner commandLineRunner() {
    return runner -> {
      final User savedUser = new User("test", "测试用户", passwordEncoder.encode("111"), 1L);

      userDao.save(savedUser);

      log.info("保存用户 [id={}]", savedUser.getId());
    };
  }
}
