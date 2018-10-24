package com.nbugs.client.attendance.web.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author hongtiancai
 * @date 2018/10/22 上午10:30
 */
class AspectUtil {
  static Map<String, String> getParams(JoinPoint joinPoint) {
    Map<String, String> res = new HashMap<>(5);
    Object[] paramArgs = joinPoint.getArgs();
    String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
    for (int i = 0, len = paramArgs.length; i < len; i++) {
      res.put(paramNames[i], paramArgs[i].toString());
    }
    return res;
  }

  static Annotation[][] getAnnotations(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    return method.getParameterAnnotations();
  }

  static Object[] getArgs(JoinPoint joinPoint) {
    return joinPoint.getArgs();
  }

  static String[] getNames(JoinPoint joinPoint) {
    return ((CodeSignature) joinPoint.getSignature()).getParameterNames();
  }
}
