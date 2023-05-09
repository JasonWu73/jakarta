package net.wuxianjie.springbootweb.auth;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.wuxianjie.springbootweb.shared.mybatis.EnumType;

import java.util.Arrays;
import java.util.Optional;

/**
 * 账号状态。
 *
 * @author 吴仙杰
 */
@Getter
@ToString
@RequiredArgsConstructor
public enum AccountStatus implements EnumType {

  /**
   * 禁用。
   */
  DISABLED(0, "账号已被管理员禁用"),

  /**
   * 启用。
   */
  ENABLED(1, "账号可正常使用");

  private static final AccountStatus[] VALUES;

  static {
    VALUES = values();
  }

  @JsonValue
  private final int code;

  private final String description;

  /**
   * 解析枚举值.
   *
   * @param code 需要被解析为枚举值的 code
   * @return 枚举值
   */
  public static Optional<AccountStatus> resolve(final Integer code) {
    return Optional.ofNullable(code)
      .flatMap(c -> Arrays.stream(VALUES)
        .filter(v -> v.code == c)
        .findFirst()
      );
  }
}
