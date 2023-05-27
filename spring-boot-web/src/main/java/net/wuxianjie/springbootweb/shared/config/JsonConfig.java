package net.wuxianjie.springbootweb.shared.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 自定义 JSON 序列化/反序列化。会应用于以下三个方面：
 *
 * <ul>
 *   <li>Controller JSON 响应结果的序列化</li>
 *   <li>Controller JSON 请求参数的反序列化</li>
 *   <li>{@link com.fasterxml.jackson.databind.ObjectMapper} 依赖注入</li>
 * </ul>
 *
 * @author 吴仙杰
 */
@Configuration
public class JsonConfig {

  /**
   * 定制 {@link com.fasterxml.jackson.databind.ObjectMapper} 的 JSON 序列化与反序列化过程。包含如下设置：
   *
   * <ul>
   *   <li>使用系统默认时区</li>
   *   <li>设置 Date 字符串格式（{@code yyyy-MM-dd HH:mm:ss}）</li>
   *   <li>设置 Java 8 LocalDate 字符串格式（{@code yyyy-MM-dd}）</li>
   *   <li>设置 Java 8 LocalDateTime 字符串格式（{@code yyyy-MM-dd HH:mm:ss}）</li>
   * </ul>
   *
   * @return 定制化的 {@code JacksonObjectMapperBuilder}
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> {
      // 使用系统默认时区
      builder.timeZone(ZoneId.systemDefault().getId());

      // 设置 Date 序列化/反序列化字符串格式
      setDateConverter(builder);

      // 设置 Java 8 LocalDate 序列化/反序列化字符串格式
      setLocalDateConverter(builder);

      // 设置 Java 8 LocalDateTime 序列化/反序列化字符串格式
      setLocalDateTimeConverter(builder);
    };
  }

  private void setDateConverter(final Jackson2ObjectMapperBuilder builder) {
    // 序列化
    final SimpleDateFormat dateFormat = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);

    builder.serializers(new DateSerializer(false, dateFormat));

    // 反序列化
    builder.deserializerByType(
      Date.class,
      new JsonDeserializer<Date>() {

        @Override
        public Date deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
          final String value = parser.getValueAsString();

          try {
            return dateFormat.parse(value);
          } catch (ParseException e) {
            throw new InvalidFormatException(parser, e.getMessage(), value, Date.class);
          }
        }
      }
    );
  }

  private void setLocalDateConverter(final Jackson2ObjectMapperBuilder builder) {
    // 序列化
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN);

    builder.serializers(new LocalDateSerializer(dateTimeFormatter));

    // 反序列化
    builder.deserializerByType(
      LocalDate.class,
      new JsonDeserializer<LocalDate>() {

        @Override
        public LocalDate deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
          return LocalDate.parse(parser.getValueAsString(), dateTimeFormatter);
        }
      }
    );
  }

  private void setLocalDateTimeConverter(final Jackson2ObjectMapperBuilder builder) {
    // 序列化
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN);

    builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));

    // 反序列化
    builder.deserializerByType(
      LocalDateTime.class,
      new JsonDeserializer<LocalDateTime>() {

        @Override
        public LocalDateTime deserialize(final JsonParser parser, DeserializationContext context) throws IOException {
          return LocalDateTime.parse(parser.getValueAsString(), dateTimeFormatter);
        }
      }
    );
  }
}
