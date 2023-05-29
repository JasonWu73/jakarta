package net.wuxianjie.springbootweb.test;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
public class StudentController {

  private List<Student> students;

  @PostConstruct
  public void loadData() {
    students = new ArrayList<>();

    students.add(new Student(1, "xianjie", "wu", "wxj@mail.com"));
    students.add(new Student(2, "jason", "wu", "jason@mail.com"));
    students.add(new Student(3, "bruce", "lee", "bruce@mail.com"));
  }

  @GetMapping("/students")
  public List<Student> getStudents() {
    return students;
  }

  @GetMapping("/students/{id}")
  public Student getUser(@PathVariable final int id) {
    if ((id < 0) || id >= students.size()) {
      throw new StudentNotFoundException("not found student id - " + id);
    }

    return students.get(id);
  }
}
