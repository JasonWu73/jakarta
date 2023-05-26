package net.wuxianjie.springbootweb.test.rest;

import jakarta.annotation.PostConstruct;
import net.wuxianjie.springbootweb.test.jpa.User2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
public class StudentController {

  private List<User2> users;

  @PostConstruct
  public void loadData() {
    users = new ArrayList<>();

    users.add(new User2("wxj", "吴仙杰", "123.321", 1L));
    users.add(new User2("jason", "JASON WU", "234.432", 2L));
    users.add(new User2("Bruce", "Bruce Lee", "345.543", 3L));
  }

  @GetMapping("/users")
  public List<User2> getUsers() {
    return users;
  }

  @GetMapping("/users/{id}")
  public User2 getUser(@PathVariable final int id) {
    return users.get(id);
  }
}
