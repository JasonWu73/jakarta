package net.wuxianjie.springbootweb.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

  private final EmployeeService employeeService;

  @GetMapping
  public List<Employee> getEmployees() {
    return employeeService.getEmployees();
  }

  @GetMapping("/{id}")
  public Employee getEmployee(@PathVariable final int id) {
    return employeeService.getEmployee(id);
  }

  @PostMapping
  public Employee addEmployee(@RequestBody final Employee employee) {
    return employeeService.addEmployee(employee);
  }

  @PutMapping
  public Employee updateEmployee(@RequestBody final Employee employee) {
    return employeeService.updateEmployee(employee);
  }
}
