package net.wuxianjie.springbootweb.test.jpa;

/**
 * 用户 DAO（Data Access Object）。
 *
 * @author 吴仙杰
 */
public interface UserDao {

  /**
   * 保存用户数据。
   *
   * @param user 需要保存的用户数据
   */
  void save(User user);
}
