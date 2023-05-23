package net.wuxianjie.springbootweb.test;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试相关 REST API。
 *
 * @author 吴仙杰
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TestController {

  private final TestService testService;
  private final TestService anotherTestService;

  @GetMapping("/scope")
  public String testBeanScope() {
    return StrUtil.format("testService == anotherTestService: {}", testService == anotherTestService);
  }
}
