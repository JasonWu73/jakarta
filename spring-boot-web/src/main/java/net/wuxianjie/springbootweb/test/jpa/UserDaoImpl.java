package net.wuxianjie.springbootweb.test.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.shared.util.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
  public void save(final User2 user) {
    entityManager.persist(user);
  }

  @Override
  public User2 findById(final long id) {
    return entityManager.find(User2.class, id);
  }

  @Override
  public List<User2> findAll() {
    final TypedQuery<User2> query = entityManager.createQuery("from User2 order by updatedAt desc", User2.class);
    return query.getResultList();
  }

  @Override
  public List<User2> findByUsernameLike(final String username) {
    final String usernameLikeValue = StringUtils.toNullableLikeValue(username);
    final TypedQuery<User2> query = entityManager.createQuery("from User2 where username like :username", User2.class);
    query.setParameter("username", usernameLikeValue);
    return query.getResultList();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(final User2 user) {
    entityManager.merge(user);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public int deleteByUsernameLike(final String username) {
    final String usernameLike = StringUtils.toNullableLikeValue(username);
    final Query query = entityManager.createQuery("delete from User2 where username like :username");
    query.setParameter("username", usernameLike);
    return query.executeUpdate();
  }
}
