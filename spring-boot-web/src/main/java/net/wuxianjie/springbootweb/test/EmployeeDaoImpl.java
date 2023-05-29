package net.wuxianjie.springbootweb.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeDaoImpl implements EmployeeDao {

  private final EntityManager entityManager;

  @Override
  public List<Employee> selectAll() {
    final TypedQuery<Employee> query = entityManager.createQuery("from Employee", Employee.class);

    return query.getResultList();
  }

  @Override
  public Employee selectById(final int employeeId) {
    return entityManager.find(Employee.class, employeeId);
  }

  @Override
  public Employee saveOrUpdate(final Employee employee) {
    return entityManager.merge(employee);
  }

  @Override
  public void deleteById(final int employeeId) {
    final Employee employeeToDel = selectById(employeeId);

    entityManager.remove(employeeToDel);
  }
}
