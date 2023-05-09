package net.wuxianjie.springbootweb.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.auth.dto.GetTokenRequest;
import net.wuxianjie.springbootweb.auth.dto.TokenResponse;
import org.springframework.web.bind.annotation.*;

/**
 * 身份验证相关 REST API。
 *
 * @author 吴仙杰
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  /**
   * 获取 Access Token。
   *
   * @param request 请求参数
   * @return Access Token 相关信息
   */
  @PostMapping("/token")
  public TokenResponse getToken(@RequestBody @Valid final GetTokenRequest request) {
    return authService.getToken(request);
  }

  /**
   * 刷新 Access Token。
   *
   * <p>刷新后，旧 Access Token 将不可用。
   *
   * @param refreshToken 需要刷新的 Access Token
   * @return Access Token 相关信息
   */
  @PutMapping("/token/{refreshToken}")
  public TokenResponse updateToken(@PathVariable final String refreshToken) {
    return authService.updateToken(refreshToken);
  }
}