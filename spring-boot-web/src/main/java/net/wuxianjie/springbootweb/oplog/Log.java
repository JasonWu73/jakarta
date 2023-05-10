package net.wuxianjie.springbootweb.oplog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于方法之上的操作日志注解。
 *
 * @author 吴仙杰
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

  String value() default "操作描述";
}
