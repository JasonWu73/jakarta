package net.wuxianjie.springbootweb.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 实现枚举值校验逻辑。
 *
 * @author 吴仙杰
 **/
@Slf4j
public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, Object> {

  private boolean isPassed = false;
  private List<Object> values;

  @Override
  public void initialize(final EnumValidator enumValidator) {
    values = new ArrayList<>();
    final Class<? extends Enum<?>> enumClass = enumValidator.value();

    Optional.ofNullable(enumClass.getEnumConstants())
      .ifPresent(enums -> Arrays.stream(enums)
        .forEach(theEnum -> {
          try {
            final Method method = theEnum.getClass().getDeclaredMethod("getCode");
            method.setAccessible(true);
            values.add(method.invoke(theEnum));
          } catch (NoSuchMethodException e) {
            isPassed = true;
            log.warn("忽略枚举值校验 [{} 不存在 getCode() 方法]", enumClass.getName());
          } catch (InvocationTargetException | IllegalAccessException e) {
            isPassed = true;
            log.warn("忽略枚举值校验 [{}.getCode() 方法执行出错]", enumClass.getName());
          }
        })
      );
  }

  @Override
  public boolean isValid(
    final Object value,
    final ConstraintValidatorContext context
  ) {
    return isPassed || value == null || values.contains(value);
  }
}
