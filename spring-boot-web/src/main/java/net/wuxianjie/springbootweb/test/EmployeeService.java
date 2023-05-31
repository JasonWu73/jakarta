package net.wuxianjie.springbootweb.test;

import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

  private final EmployeeRepository employeeRepository;

  public List<Employee> getEmployees() {
    return employeeRepository.findAllByOrderByLastName();
  }

  public void save(final Employee employee) {
    employeeRepository.save(employee);
  }

  public Employee getEmployee(final int employeeId) {
    // get employee by id from db
    return employeeRepository.findById(employeeId)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "employee not found id - " + employeeId));
  }

  public void deleteEmployee(final int employeeId) {
    employeeRepository.deleteById(employeeId);
  }
}
