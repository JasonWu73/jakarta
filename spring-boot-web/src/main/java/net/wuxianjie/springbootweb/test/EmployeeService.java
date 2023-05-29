package net.wuxianjie.springbootweb.test;

import java.util.List;

public interface EmployeeService {

  List<Employee> getEmployees();

  Employee getEmployee(int employeeId);

  Employee addEmployee(Employee employee);

  Employee updateEmployee(Employee employee);

  String deleteEmployee(int employeeId);
}
