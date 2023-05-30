package net.wuxianjie.springbootweb.test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
public class ThymeleafTestController {

  @GetMapping("/test")
  public String sayHello(final Model model) {
    model.addAttribute("theDate", new Date());

    return "test";
  }
}
