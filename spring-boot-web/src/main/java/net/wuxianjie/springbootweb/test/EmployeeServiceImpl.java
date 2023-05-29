package net.wuxianjie.springbootweb.test;

import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.shared.restapi.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    return Optional.ofNullable(employeeDao.selectById(employeeId))
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "not found employee"));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Employee addEmployee(final Employee employee) {
    return employeeDao.saveOrUpdate(employee);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Employee updateEmployee(final Employee employee) {
    return employeeDao.saveOrUpdate(employee);
  }

  @Override
  public void deleteEmployee(final int employeeId) {
    employeeDao.deleteById(employeeId);
  }
}
