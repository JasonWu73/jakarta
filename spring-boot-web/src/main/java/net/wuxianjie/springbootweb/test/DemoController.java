package net.wuxianjie.springbootweb.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/thymeleaf")
public class DemoController {

  @GetMapping("/")
  public String showHome() {
    return "demo/home";
  }

  @GetMapping("/power")
  public String showPower() {
    return "demo/power";
  }
}
