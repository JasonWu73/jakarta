package net.wuxianjie.springbootweb.test;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试相关 REST API。
 *
 * @author 吴仙杰
 */
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

  private final TestService testService;
  private final TestService anotherTestService;

  private final ThirdLibrarySdk sdk;
  private final ThirdLibrarySdk sdk2;

  public TestController(
    final TestService testService,
    final TestService anotherTestService,
    @Qualifier("thirdLibrary") final ThirdLibrarySdk sdk,
    @Qualifier("sdk2") final ThirdLibrarySdk sdk2
  ) {
    this.testService = testService;
    this.anotherTestService = anotherTestService;
    this.sdk = sdk;
    this.sdk2 = sdk2;
  }

  /**
   * 测试 Bean Scope。
   *
   * @return 打印结果
   */
  @GetMapping("/scope")
  public String testBeanScope() {
    return StrUtil.format("testService == anotherTestService: {}", testService == anotherTestService);
  }

  @GetMapping("/3-rd")
  public ResponseEntity<String> testThirdLibrary() {
    final String version1 = sdk.getVersion();
    final String version2 = sdk2.getVersion();
    return ResponseEntity.ok(version1 + "\n" + version2);
  }
}
