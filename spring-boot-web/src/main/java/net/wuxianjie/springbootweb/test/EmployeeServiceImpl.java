package net.wuxianjie.springbootweb.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeDao employeeDao;

  @Override
  public List<Employee> getEmployees() {
    return employeeDao.selectAll();
  }
}
