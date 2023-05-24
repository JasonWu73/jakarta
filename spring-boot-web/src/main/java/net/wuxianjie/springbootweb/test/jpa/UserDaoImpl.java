package net.wuxianjie.springbootweb.test.jpa;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 实现用户 DAO 操作。
 *
 * @author 吴仙杰
 */
@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

  private final EntityManager entityManager;

  @Override
  @Transactional(rollbackFor = Exception.class) // 对于 JPA 来说，更新操作必须要开启事物
  public void save(final User user) {
    entityManager.persist(user);
  }

  @Override
  public User findById(final long id) {
    return entityManager.find(User.class, id);
  }
}
