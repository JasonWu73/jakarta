package net.wuxianjie.springbootweb.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/employees")
@RequiredArgsConstructor
public class EmployeeV2Controller {

  private final EmployeeService2 employeeService;

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

  @DeleteMapping("/{id}")
  public String deleteEmployee(@PathVariable final int id) {
    return employeeService.deleteEmployee(id);
  }
}
