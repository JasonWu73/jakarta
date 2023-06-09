package net.wuxianjie.springbootweb;

import net.wuxianjie.springbootweb.auth.AuthProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot 启动类。
 *
 * <p>将指明项目版本号的 API 置于此处还是比较合适的。
 *
 * @author 吴仙杰
 */
@SpringBootApplication
@RestController
@RequestMapping("/api/v1")
public class Application {

  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }

  /**
   * 获取项目版本号。
   *
   * @return 项目版本号信息
   */
  @GetMapping("/version")
  public ResponseEntity<Version> getVersion() {
    return ResponseEntity.ok(new Version("v1.0.0", AuthProps.TOKEN_ISSUER, "吴仙杰"));
  }

  /**
   * 项目版本号信息。
   *
   * @param version 版本号
   * @param appName 应用名
   * @param developer 开发者
   */
  private record Version(String version, String appName, String developer) {}
}
