package net.wuxianjie.springbootweb.oplog;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wuxianjie.springbootweb.auth.AuthUtils;
import net.wuxianjie.springbootweb.auth.dto.AuthData;
import net.wuxianjie.springbootweb.shared.util.ServletUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 记录操作日志 AOP。
 *
 * @author 吴仙杰
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OpLogAspect {

  private final OpLogMapper opLogMapper;

  /**
   * 仅记录操作成功的日志。
   *
   * @param joinPoint {@link ProceedingJoinPoint}
   * @return 方法返回结果
   */
  @Around("@annotation(Log)")
  public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {
    // 先记录请求的时间
    final LocalDateTime requestTime = LocalDateTimeUtil.now();

    // 执行后续方法，并返回最终结果
    final Object result = joinPoint.proceed();

    // 记录操作日志
    final OpLog opLog = new OpLog();

    // 请求时间
    opLog.setRequestTime(requestTime);

    // 客户端 IP
    final HttpServletRequest request = ServletUtils.getCurrentRequest().orElseThrow();
    final String clientIP = JakartaServletUtil.getClientIP(request);
    opLog.setClientIp(clientIP);

    // 用户名
    final String username = AuthUtils.getCurrentUser()
      .map(AuthData::username)
      .orElse(null);
    opLog.setUsername(username);

    // 请求 API
    final String requestUri = request.getRequestURI();
    final String requestMethod = request.getMethod();
    final String api = StrUtil.format("[{}] {}", requestMethod, requestUri);
    opLog.setApi(api);

    // 操作描述
    final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    final Method method = signature.getMethod();
    final Log annotation = method.getAnnotation(Log.class);
    final String message = annotation.value();
    opLog.setMessage(message);

    // 保存至数据库
    if (opLogMapper.insert(opLog) != 1) {
      log.error("新增操作日志失败");
    }

    return result;
  }
}
