package net.wuxianjie.springbootweb.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Token 鉴权相关的配置项。
 *
 * @author 吴仙杰
 **/
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthProps {

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
   * Token 过期时间，单位：秒。
   */
  public static final int TOKEN_EXPIRATION_SEC = 1800;

  /**
   * Token 签发者，即当前系统名。
   */
  public static final String TOKEN_ISSUER = "设备管控平台";

  /**
   * Token 签名密钥。
   */
  @NotBlank(message = "Token 签名密钥不能为空")
  private String tokenKey;
}
