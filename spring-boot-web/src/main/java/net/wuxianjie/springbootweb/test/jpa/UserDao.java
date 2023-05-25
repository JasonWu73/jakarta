package net.wuxianjie.springbootweb.test.jpa;

import java.util.List;

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
  void save(User2 user);

  /**
   * 根据用户 id 获取用户数据。
   *
   * @param id 需要查找的用户 id
   * @return 用户数据
   */
  User2 findById(long id);

  /**
   * 获取所有用户数据。
   *
   * @return 用户列表
   */
  List<User2> findAll();

  /**
   * 根据用户名模糊搜索用户数据。
   *
   * @param username 需要查找的用户名
   * @return 用户列表
   */
  List<User2> findByUsernameLike(String username);

  /**
   * 更新用户数据。
   *
   * @param user 要更新的用户数据
   */
  void update(User2 user);

  /**
   * 通过用户名模糊搜索进行删除用户。
   *
   * @param username 需要查找的用户名
   * @return 删除的用户数
   */
  int deleteByUsernameLike(String username);
}
