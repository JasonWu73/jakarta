package net.wuxianjie.springbootweb.shared.mybatis;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

/**
 * 枚举类型与数据库 int 类型的类型转换器。
 *
 * <p>需在配置文件（`application.[properties|yml]`）中指定：
 *
 * <pre>{@code
 *   mybatis.type-handlers-package: net.wuxianjie.springbootweb.shared.mybatis
 * }</pre>
 *
 * @author 吴仙杰
 */
@NoArgsConstructor
@AllArgsConstructor
public class EnumTypeHandler<E extends Enum<?> & EnumType> extends BaseTypeHandler<EnumType> {

  private Class<E> enumType;

  @Override
  public void setNonNullParameter(
    final PreparedStatement preparedStatement,
    final int parameterIndex,
    final EnumType parameter,
    final JdbcType jdbcType
  ) throws SQLException {
    preparedStatement.setInt(parameterIndex, parameter.getCode());
  }

  @Override
  public EnumType getNullableResult(
    final ResultSet resultSet,
    final String columnLabel
  ) throws SQLException {
    return toNullableEnum(enumType, resultSet.getInt(columnLabel));
  }

  @Override
  public EnumType getNullableResult(
    final ResultSet resultSet,
    final int columnIndex
  ) throws SQLException {
    return toNullableEnum(enumType, resultSet.getInt(columnIndex));
  }

  @Override
  public EnumType getNullableResult(
    final CallableStatement callableStatement,
    final int columnIndex
  ) throws SQLException {
    return toNullableEnum(enumType, callableStatement.getInt(columnIndex));
  }

  private E toNullableEnum(final Class<E> enumClass, final int value) {
    return Optional.ofNullable(enumClass.getEnumConstants())
      .flatMap(enums -> Arrays.stream(enums)
        .filter(e -> e.getCode() == value)
        .findFirst()
      )
      .orElse(null);
  }
}
