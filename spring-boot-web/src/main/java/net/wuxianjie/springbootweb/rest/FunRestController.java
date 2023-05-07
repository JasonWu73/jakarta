package net.wuxianjie.springbootweb.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class FunRestController {

  @GetMapping("/hello")
  public String hello(@RequestParam(value = "name", defaultValue = "World") final String name) {
    return String.format("Hello %s", name);
  }
}
