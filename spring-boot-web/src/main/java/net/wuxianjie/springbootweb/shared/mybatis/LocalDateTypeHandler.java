package net.wuxianjie.springbootweb.shared.mybatis;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 实现 {@link LocalDate} 类型与数据库 date 类型的类型转换器。
 *
 * <p>本转换器是通过将数据库 date 类型识别为字符串进行转换。
 *
 * <p>需在配置文件（`application.[properties|yml]`）中指定：
 *
 * <pre>{@code
 *   mybatis.type-handlers-package: net.wuxianjie.springbootweb.shared.mybatis
 * }</pre>
 *
 * @author 吴仙杰
 */
public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {

  @Override
  public void setNonNullParameter(
    final PreparedStatement preparedStatement,
    final int parameterIndex,
    final LocalDate parameter,
    final JdbcType jdbcType
  ) throws SQLException {
    preparedStatement.setString(parameterIndex, LocalDateTimeUtil.formatNormal(parameter));
  }

  @Override
  public LocalDate getNullableResult(
    final ResultSet resultSet,
    final String columnLabel
  ) throws SQLException {
    return toNullableLocalDate(resultSet.getString(columnLabel));
  }

  @Override
  public LocalDate getNullableResult(
    final ResultSet resultSet,
    final int columnIndex
  ) throws SQLException {
    return toNullableLocalDate(resultSet.getString(columnIndex));
  }

  @Override
  public LocalDate getNullableResult(
    final CallableStatement callableStatement,
    final int columnIndex
  ) throws SQLException {
    return toNullableLocalDate(callableStatement.getString(columnIndex));
  }

  private static LocalDate toNullableLocalDate(String dateStr) {
    return Optional.ofNullable(dateStr)
      .map(s -> LocalDateTimeUtil.parseDate(dateStr, DateTimeFormatter.ISO_DATE))
      .orElse(null);
  }
}
