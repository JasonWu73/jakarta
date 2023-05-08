package net.wuxianjie.springbootweb.shared.security;

/**
 * 实现 Token 身份验证必须要实现的接口，详见 {@link TokenAuthFilter}。
 *
 * @author 吴仙杰
 **/
public interface TokenAuth {

  /**
   * 对 Access Token 执行身份验证，并返回该 Token 所对应的用户名。
   *
   * <p>需要实现的逻辑：
   *
   * <ol>
   *   <li>验证 JWT Token 本身（格式）是否合法</li>
   *   <li>解析 JWT Token 获取用户名及类型</li>
   *   <li>Token 类型必须为 {@link SecurityProps#TOKEN_TYPE_ACCESS}</li>
   *   <li>通过用户名获取用户数据</li>
   * </ol>
   *
   * @param accessToken 需要进行身份验证的 Access Token
   * @return 用户信息
   * @throws Exception 当身份验证失败时抛出
   */
  AuthData authenticate(String accessToken) throws Exception;
}
