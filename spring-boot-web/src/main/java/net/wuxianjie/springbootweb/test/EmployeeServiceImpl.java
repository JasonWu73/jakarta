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

  @Override
  public Employee getEmployee(final int employeeId) {
    return employeeDao.selectById(employeeId);
  }

  @Override
  public Employee addEmployee(final Employee employee) {
    return employeeDao.saveOrUpdate(employee);
  }

  @Override
  public Employee updateEmployee(final Employee employee) {
    return employeeDao.saveOrUpdate(employee);
  }

  @Override
  public void deleteEmployee(final int employeeId) {
    employeeDao.deleteById(employeeId);
  }
}
