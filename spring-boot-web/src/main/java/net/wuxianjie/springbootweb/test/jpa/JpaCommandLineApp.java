package net.wuxianjie.springbootweb.test.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

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
      createUser();
//      readUser();
//      queryAllUsers();
//      queryUsersByUsername();
//      updateUser();
//      deleteUsers();
    };
  }

  private void createUser() {
    // 创建用户
    final User2 savedUser1 = new User2("test1", "测试用户1", passwordEncoder.encode("111"), 1L);
    final User2 savedUser2 = new User2("test2", "测试用户2", passwordEncoder.encode("111"), 1L);

    // 保存用户
    userDao.save(savedUser1);

    // 打印保存后的用户 id
    log.info("保存用户[nickname={};id={}]", savedUser1.getNickname(), savedUser1.getId());

    userDao.save(savedUser2);
    log.info("保存用户[nickname={};id={}]", savedUser2.getNickname(), savedUser2.getId());
  }

  private void readUser() {
    // 需要查找的用户 id
    final long userId = 1;

    // 查找用户
    final User2 findedUser = userDao.findById(userId);

    // 打印找到的用户数据
    log.info("id 为 {} 的用户数据: {}", userId, findedUser);
  }

  private void queryAllUsers() {
    // 查找所有用户
    final List<User2> users = userDao.findAll();

    // 遍例并打印所有用户
    users.forEach(System.out::println);
  }

  private void queryUsersByUsername() {
    // 查找用户名中 te 字符串的用户数据
    final String username = "te";
    final List<User2> users = userDao.findByUsernameLike(username);

    // 遍例并打印用户列表
    users.forEach(System.out::println);
  }

  private void updateUser() {
    // 根据 id 获取用户
    final long userId = 8;
    final User2 user = userDao.findById(userId);

    // 将用户名更新为 "test_update"
    user.setUsername("test_update");

    // 更新用户
    userDao.update(user);

    // 打印更新后的用户数据
    log.info("更新后的用户数据: {}", user);
  }

  private void deleteUsers() {
    // 删除所有用户名中包含 "test" 字符串的用户
    final String username = "test";
    final int deletedNums = userDao.deleteByUsernameLike(username);
    log.info("一共删除了 {} 个用户名中包含 {} 字符串的用户", deletedNums, username);
  }
}
