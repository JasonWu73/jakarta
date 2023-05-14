package net.wuxianjie.springbootweb.auth;

import net.wuxianjie.springbootweb.auth.dto.GetTokenRequest;
import net.wuxianjie.springbootweb.auth.dto.TokenResponse;
import org.springframework.http.ResponseEntity;

/**
 * 身份验证相关处理。
 *
 * @author 吴仙杰
 */
public interface AuthService {

  /**
   * 获取 Access Token。
   *
   * @param request 请求参数
   * @return Access Token 相关信息
   */
  ResponseEntity<TokenResponse> getToken(final GetTokenRequest request);

  /**
   * 刷新 Access Token。
   *
   * <p>刷新后，旧 Access Token 将不可用。
   *
   * @param refreshToken 需要刷新的 Access Token
   * @return Access Token 相关信息
   */
  ResponseEntity<TokenResponse> updateToken(final String refreshToken);
}
