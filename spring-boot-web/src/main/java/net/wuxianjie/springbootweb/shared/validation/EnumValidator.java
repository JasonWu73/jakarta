package net.wuxianjie.springbootweb.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 枚举值校验注解，例如：
 *
 * <pre>{@code
 *   @RequiredArgsConstructor
 *   @Getter
 *   @ToString
 *   public enum Type {
 *
 *     ME(1);
 *
 *     @JsonValue
 *     private final int code;
 *   }
 *
 *   @RestController
 *   @RequestMapping("/api/v1")
 *   public class TypeController {
 *
 *     @GetMapping("/test")
 *     public test(@RequestBody @Valid final TestRequest request) {
 *       // ...
 *     }
 *
 *     @Data
 *     private static class TestRequest {
 *
 *       @EnumValidator(value = Type.class, message = "类型不合法")
 *       private Integer type;
 *     }
 *   }
 * }</pre>
 *
 * <p>注意：{@code null} 值被认为是合法的。
 *
 * @author 吴仙杰
 **/
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidatorImpl.class)
@Repeatable(EnumValidator.List.class)
public @interface EnumValidator {

  Class<? extends Enum<?>> value();

  String message() default "类型不合法";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RetentionPolicy.RUNTIME)
  @interface List {

    EnumValidator[] value();
  }
}
