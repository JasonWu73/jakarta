package net.wuxianjie.springbootweb.test;

import java.util.List;

public interface EmployeeDao {

  List<Employee> selectAll();

  Employee selectById(int employeeId);

  Employee saveOrUpdate(Employee employee);

  void deleteById(int employeeId);
}
