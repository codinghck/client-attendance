package com.nbugs.client.attendance.web.aspect;

import com.alibaba.fastjson.JSONObject;
import com.hck.util.base.BaseUtil;
import com.hck.util.request.RqResult;
import java.util.Arrays;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author hongtiancai
 * @date 2018/10/22 上午10:30
 * 日志的 order 需要最小，其次异常，其次校验
 */
@Aspect
@Component
@Order(10)
@Log4j2
public class LogHandler {
  @Pointcut("execution(public * com..controller..*.*(..))")
  public void logHandle() {}

  @Around("logHandle()")
  @SuppressWarnings("unchecked")
  public  <T> RqResult<T> doLog(ProceedingJoinPoint pjp) throws Throwable {
    RqResult<T> res = (RqResult<T>)pjp.proceed();
    log.info("className = " + pjp.getTarget().getClass().getName()
        + ", methodName = " + pjp.getSignature().getName()
        + ", paramNames = " + Arrays.toString(((CodeSignature) pjp.getSignature()).getParameterNames())
        + ", paramArgs = " + Arrays.toString(pjp.getArgs())
        + ", returnValue = " + JSONObject.toJSONString(res)
        + ", time = " + BaseUtil.getCurrTime());
    return res;
  }
}
