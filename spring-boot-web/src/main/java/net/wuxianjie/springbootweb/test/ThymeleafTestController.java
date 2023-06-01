package net.wuxianjie.springbootweb.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafTestController {

  @GetMapping("/")
  public String showHome() {
    return "home";
  }
}
