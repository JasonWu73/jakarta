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
@RequiredArgsConstructor
@Getter
@ToString
public enum AccountStatus implements EnumType {

  /**
   * 禁用。
   */
  DISABLED(0),
  /**
   * 启用。
   */
  ENABLED(1);

  private static final AccountStatus[] VALUES;

  static {
    VALUES = values();
  }

  /**
   * 代表枚举值的常量值。
   */
  @JsonValue
  private final int code;

  /**
   * 解析枚举值。
   *
   * @param code 需要被解析为枚举值的 code
   * @return 枚举值
   */
  public static Optional<AccountStatus> resolve(final Integer code) {
    return Optional.ofNullable(code)
      .flatMap(c -> Arrays.stream(VALUES)
        .filter(v -> v.code == c)
        .findFirst());
  }
}
