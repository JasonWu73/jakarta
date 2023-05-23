package net.wuxianjie.springbootweb.test;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 模拟第三方非 Spring Bean 库。
 *
 * @author 吴仙杰
 */
@Slf4j
public class ThirdLibrarySdk {

  private final String secret;

  public ThirdLibrarySdk(final String secret) {
    this.secret = secret;
    log.info("构造函数: {}", getClass().getSimpleName());
  }

  /**
   * 打印模拟的版本号。
   *
   * @return 版本号信息
   */
  public String getVersion() {
    return StrUtil.format("版本号: v1.0.0;密钥: {}", secret);
  }
}
