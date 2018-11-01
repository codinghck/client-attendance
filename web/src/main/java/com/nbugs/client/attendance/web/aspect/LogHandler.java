package com.nbugs.client.attendance.web.aspect;

import com.hongtiancai.base.util.common.base.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author hck
 * @date 2018/10/22 上午10:30
 * 日志的 order 需要最小，其次异常，其次校验
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class LogHandler {
  @Pointcut("execution(public * com..controller..*.*(..))")
  public void logHandle() {}

  @Around("logHandle()")
  public Object doLog(ProceedingJoinPoint pjp) throws Throwable {
    Object res = pjp.proceed();
    LogUtil.aspectLogWithRes(log, pjp, res);
    return res;
  }
}
