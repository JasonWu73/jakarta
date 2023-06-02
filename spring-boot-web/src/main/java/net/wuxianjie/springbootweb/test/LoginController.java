package net.wuxianjie.springbootweb.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

  @GetMapping("/showMyLoginPage")
  public String showMyLoginPage() {
//    return "plain-login";
    return "fancy-login";
  }

  @GetMapping("/")
  public String showHome() {
    return "home";
  }

  @GetMapping("/leaders")
  public String showLeaders() {
    return "leaders";
  }
}
