package net.wuxianjie.springbootweb.test;

import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService2 {

  private final EmployeeRepository employeeRepository;

  public List<Employee> getEmployees() {
    return employeeRepository.findAll();
  }

  public Employee getEmployee(final int id) {
    return employeeRepository.findById(id)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "employee not found id - " + id));
  }

  public Employee addEmployee(final Employee employee) {
    return employeeRepository.save(employee);
  }

  public Employee updateEmployee(final Employee employee) {
    return employeeRepository.save(employee);
  }

  public String deleteEmployee(final int id) {
    employeeRepository.deleteById(id);

    return "Deleted employee id - " + id;
  }
}
