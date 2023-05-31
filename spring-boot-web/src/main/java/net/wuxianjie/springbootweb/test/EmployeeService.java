package net.wuxianjie.springbootweb.test;

import lombok.RequiredArgsConstructor;
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
}
