package net.wuxianjie.springbootweb.test.mvc;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {

  @GetMapping("/")
  public String showHome() {
    return "home";
  }

  @GetMapping("/login")
  public String showLogin() {
    return "login";
  }

  @GetMapping("/users")
  @PreAuthorize("hasAuthority('user_view')")
  public String showUser() {
    return "user";
  }

  @GetMapping("/users/add")
  @PreAuthorize("hasAuthority('user_add')")
  public String showAddUser() {
    return "user-add";
  }

  @GetMapping("/access-denied")
  public String showAccessDenied() {
    return "403";
  }

  @GetMapping("/power")
  public String showPower() {
    return "power";
  }
}
