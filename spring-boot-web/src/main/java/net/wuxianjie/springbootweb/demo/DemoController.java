package net.wuxianjie.springbootweb.demo;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DemoController {

  @Value("${demo.author.name}")
  private String authorName;

  @Value("${demo.author.email}")
  private String authorEmail;

  /**
   * 获取自定义配置。
   *
   * <p>无需任何额外配置即可读取 `application.yml` 中的自定义配置项。
   *
   * @return 自定义配置项
   */
  @GetMapping("/custom-config-props")
  private ResponseEntity<Map<String, String>> getCustomConfigProperties() {
    return ResponseEntity.ok(Map.of("authorName", authorName, "authorEmail", authorEmail));
  }
}
