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

//  @Bean
  public CommandLineRunner commandLineRunner() {
    return runner -> {
      // 创建用户
      final User savedUser1 = new User("test1", "测试用户1", passwordEncoder.encode("111"), 1L);
      final User savedUser2 = new User("test2", "测试用户2", passwordEncoder.encode("111"), 1L);

      // 保存用户
      userDao.save(savedUser1);

      // 打印保存后的用户 id
      log.info("保存用户[nickname={};id={}]", savedUser1.getNickname(), savedUser1.getId());

      userDao.save(savedUser2);
      log.info("保存用户[nickname={};id={}]", savedUser2.getNickname(), savedUser2.getId());
    };
  }
}
