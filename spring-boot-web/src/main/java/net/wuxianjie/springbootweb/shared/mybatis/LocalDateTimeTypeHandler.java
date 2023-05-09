package net.wuxianjie.springbootweb.shared.mybatis;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * {@link  LocalDateTime} 类型与数据库 datetime 类型的类型转换器。
 *
 * <p>需在配置文件（`application.[properties|yml]`）中指定：
 *
 * <pre>{@code
 *   mybatis.type-handlers-package: net.wuxianjie.springbootweb.shared.mybatis
 * }</pre>
 *
 * @author 吴仙杰
 */
public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

  @Override
  public void setNonNullParameter(
    final PreparedStatement preparedStatement,
    final int parameterIndex,
    final LocalDateTime parameter,
    final JdbcType jdbcType
  ) throws SQLException {
    preparedStatement.setString(parameterIndex, LocalDateTimeUtil.formatNormal(parameter));
  }

  @Override
  public LocalDateTime getNullableResult(
    final ResultSet resultSet,
    final String columnLabel
  ) throws SQLException {
    return toNullableLocalDateTime(resultSet.getTimestamp(columnLabel));
  }

  @Override
  public LocalDateTime getNullableResult(
    final ResultSet resultSet,
    final int columnIndex
  ) throws SQLException {
    return toNullableLocalDateTime(resultSet.getTimestamp(columnIndex));
  }

  @Override
  public LocalDateTime getNullableResult(
    final CallableStatement callableStatement,
    final int columnIndex
  ) throws SQLException {
    return toNullableLocalDateTime(callableStatement.getTimestamp(columnIndex));
  }

  private static LocalDateTime toNullableLocalDateTime(final Timestamp timestamp) {
    return Optional.ofNullable(timestamp)
      .map(Timestamp::toLocalDateTime)
      .orElse(null);
  }
}
