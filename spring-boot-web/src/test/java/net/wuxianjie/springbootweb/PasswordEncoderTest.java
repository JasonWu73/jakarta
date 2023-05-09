package net.wuxianjie.springbootweb;

import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

  @Test
  void testEncodePassword() {
    final String rawPassword = "111";
    final String hashedPassword = new BCryptPasswordEncoder().encode(rawPassword);
    Console.log("原密码: {}, 编码后: {}", rawPassword, hashedPassword);
  }
}
