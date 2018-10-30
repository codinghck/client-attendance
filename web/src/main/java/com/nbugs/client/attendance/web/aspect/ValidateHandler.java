package com.nbugs.client.attendance.web.aspect;

import com.alibaba.fastjson.JSONObject;
import com.hongtiancai.base.util.common.base.BaseUtil;
import com.hongtiancai.base.util.validation.checker.CheckHandler;
import com.hongtiancai.base.util.validation.exception.ListParamException;
import com.hongtiancai.base.util.validation.exception.ListParamsException;
import com.hongtiancai.base.util.validation.pojo.ErrListDataOut;
import com.hongtiancai.base.util.validation.request.ErrData;
import com.hongtiancai.base.util.validation.request.RqResult;
import com.hongtiancai.base.util.validation.request.RqResultHandler;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
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
@Log4j2
@Aspect
@Component
@Order(12)
public class ValidateHandler {
  @Pointcut("execution(public * com..controller..*.*(..))")
  public void allApiValidate() {}

  @Around("allApiValidate()")
  public Object deBefore(ProceedingJoinPoint pjp) throws Throwable {
    String[] paramNames = ((CodeSignature) pjp.getSignature()).getParameterNames();
    Object[] paramArgs = pjp.getArgs();
    Annotation[][] annotations = AspectUtil.getAnnotations(pjp);
    Exception e = checkParamByAnnotations(paramNames, paramArgs, annotations);

    RqResult<Object> res = checkListParamsException(paramNames, paramArgs, pjp, e);

    return null == res ? pjp.proceed() : res;
  }

  private List<ErrData> exceptionsToErrDatas(List<ListParamException> es) {
    List<ErrData> res = new ArrayList<>();
    for (ListParamException lpe : es) {
      res.add(new ErrData(lpe.getElementIdValue(), lpe.getMsg()));
    }
    return res;
  }

  private Exception checkParamByAnnotations(
      String[] paramNames, Object[] args, Annotation[][] ans) throws Exception {
    Exception e = null;
    for (int i = 0, len = args.length; i < len; i++) {
      e = CheckHandler.check(paramNames[i], args[i], ans[i]);
    }
    return e;
  }

  @SuppressWarnings("unchecked")
  private RqResult<Object> checkListParamsException(
      String[] paramNames, Object[] paramArgs, ProceedingJoinPoint pjp, Exception e) throws Throwable {
    if (e instanceof ListParamsException) {
      int i = BaseUtil.findStrInArr(paramNames, ((ListParamsException) e).getListParamName());

      if (i != -1 && needListParamsCheck(pjp)) {
        paramArgs[i] = JSONObject.toJSONString(((ListParamsException) e).getNoErrList());
        List<ErrData> invalidData1 = exceptionsToErrDatas(((ListParamsException) e).getListParamExceptions());

        RqResult<ErrListDataOut> res = (RqResult<ErrListDataOut>) pjp.proceed(paramArgs);
        List<ErrData> invalidData2 = res.getResult().getInvalidData();

        if (null != invalidData2 && !invalidData2.isEmpty()) {
          invalidData1.addAll(invalidData2);
        }

        ErrListDataOut dataOut = new ErrListDataOut(
            AspectUtil.getParams(pjp).get("transactionId"), invalidData1);
        return new RqResultHandler<>().getSuccessInvalidRqRes(dataOut);
      }
    }
    return null;
  }

  private boolean needListParamsCheck(ProceedingJoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    return methodName.endsWith("WihListParamsCheck");
  }
}
