package net.wuxianjie.springbootweb.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Demo 学习相关 REST API。
 *
 * @author 吴仙杰
 */
@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

  @Value("${demo.author.name}")
  private String authorName;

  @Value("${demo.author.email}")
  private String authorEmail;

  private DemoService demoService;

  @Autowired
  @Qualifier("demoService2Impl")
  private DemoService demoService2;

  /**
   * 获取自定义配置。
   *
   * <p>无需任何额外配置即可读取 `application.yml` 中的自定义配置项。
   *
   * @return 自定义配置项
   */
  @GetMapping("/custom-config-props")
  public ResponseEntity<Map<String, String>> getCustomConfigProperties() {
    return ResponseEntity.ok(Map.of("authorName", authorName, "authorEmail", authorEmail));
  }

  /**
   * Setter 方法依赖注入。
   *
   * @return Hello World
   */
  @GetMapping("/setter-injection")
  public ResponseEntity<String> getSetterInjection() {
    return ResponseEntity.ok(demoService.helloWorld());
  }

  /**
   * Field Injection.
   *
   * @return Hello World
   */
  @GetMapping("/field-injection")
  public ResponseEntity<String> getFieldInjection() {
    return ResponseEntity.ok(demoService2.helloWorld());
  }

  /**
   * Setter Injection.
   *
   * @param demoService {@link DemoServiceImpl}
   */
  @Autowired
  public void setDemoService(final DemoServiceImpl demoService) {
    this.demoService = demoService;
  }
}
