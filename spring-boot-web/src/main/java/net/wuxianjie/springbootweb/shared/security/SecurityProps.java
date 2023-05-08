package net.wuxianjie.springbootweb.shared.security;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Web Security 配置项。
 *
 * @author 吴仙杰
 **/
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityProps {

  /**
   * JWT Payload - 用户名。
   */
  public static final String JWT_PAYLOAD_USERNAME = "username";

  /**
   * JWT Payload - Token 类型。
   */
  public static final String JWT_PAYLOAD_TYPE = "type";

  /**
   * JWT Payload - Access Token 类型值。
   */
  public static final String TOKEN_TYPE_ACCESS = "access";

  /**
   * JWT Payload - Refresh Token 类型值。
   */
  public static final String TOKEN_TYPE_REFRESH = "refresh";

  /**
   * Token 签名密钥。
   */
  @NotBlank(message = "Token 签名密钥不能为空")
  private String tokenKey;

  /**
   * Token 过期时间，单位：秒.
   */
  @Min(message = "Token 过期时间至少要大于 600 秒", value = 600)
  private int expirationSec;

  /**
   * 定义 Public API Endpoint，格式为 {@code [[HTTP_METHOD] API_PATH]}。
   *
   * <p>其中 HTTP 请求方法使用大写字母，如 GET、POST、PUT、DELETE 等。
   *
   * <p>配置示例：“GET /api/v1/token”、“/api/&#42;/public/&#42;&#42;”。
   *
   * <ul>
   *   <li>其中 HTTP 请求方法 HTTP_METHOD 可省略，如：“GET /api/v1/token”</li>
   *   <li>URI 符合 ANT 风格路径模式，详见 {@link org.springframework.util.AntPathMatcher}</li>
   * </ul>
   *
   * <p>API Endpoint 匹配说明：
   *
   * <ul>
   *   <li>按顺序比较 API Endpoint，符合则退出后续比较</li>
   *   <li>规定所有 API 地址都以 “/api/” 开头，即默认所有 “/api/**” 请求都需经过 Token 鉴权</li>
   *   <li>所有非 API 请求（即静态资源，SPA 单页面应用）都可直接访问</li>
   * </ul>
   */
  @NotEmpty(message = "Public API Endpoint 不能为空")
  private List<String> publicApis;

  /**
   * 解析 {@link #publicApis}。
   *
   * @return API Endpoint 解析结果，其中 {@link ApiPair#method()} 可能为 {@code null}
   */
  public List<ApiPair> parsePublicApis() {
    return publicApis.stream()
      .filter(api -> {
        // [[HTTP_METHOD] API_PATH]
        final List<String> parts = StrUtil.splitTrim(api, " ");
        return parts.size() == 1 || parts.size() == 2;
      })
      .map(api -> {
        // [[HTTP_METHOD] API_PATH]
        final List<String> parts = StrUtil.splitTrim(api, " ");

        // [API_PATH]
        if (parts.size() == 1) {
          return new ApiPair(parts.get(0), null);
        }

        // [HTTP_METHOD API_PATH]
        return new ApiPair(parts.get(1), HttpMethod.valueOf(parts.get(0)));
      })
      .toList();
  }

  /**
   * {@link #publicApis} 的解析结果。
   *
   * @param uri 请求 URI
   * @param method 请求方法，可能为 {@code null}
   */
  public record ApiPair(String uri, HttpMethod method) {
  }
}
