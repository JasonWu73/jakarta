package net.wuxianjie.springbootweb.demo;

import org.springframework.stereotype.Service;

/**
 * Demo 学习相关处理。
 *
 * @author 吴仙杰
 */
@Service
public class DemoService2Impl implements DemoService {

  /**
   * 测试方法。
   *
   * @return Hello World
   */
  public String helloWorld() {
    return "Hello World 2";
  }
}
