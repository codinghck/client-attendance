package com.nbugs.client.attendance.web.aspect;

import com.alibaba.fastjson.JSONObject;
import com.hck.util.base.BaseUtil;
import com.hck.util.request.ErrData;
import com.hck.util.request.ErrorCode;
import com.hck.util.request.RqResult;
import com.hck.util.request.RqResultHandler;
import com.hck.util.validate.exception.ListParamException;
import com.hck.util.validate.exception.ListParamsException;
import com.hck.util.validate.exception.ValidateException;
import com.hck.util.validate.pojo.ErrListDataOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author 洪天才
 * 日志的 order 需要最小，其次异常，其次校验
 */
@Aspect
@Component
@Order(11)
@Log4j2
public class ExceptionHandler {

  @Pointcut("execution(public * com..controller..*.*(..))")
  public void exceptionHandle() {}

  @Around("exceptionHandle()")
  @SuppressWarnings("unchecked")
  public <T> RqResult<T> doWhenThrow(ProceedingJoinPoint pjp) throws Throwable {
    RqResultHandler<T> handler = new RqResultHandler<>();
    try {
      return (RqResult<T>)pjp.proceed();
    } catch (ValidateException e) {
      log(e, AspectUtil
          .getNames(pjp), AspectUtil.getArgs(pjp));
      return handler.getErrRqRes(ErrorCode.PARAMETER_ERROR, e);
    } catch (ListParamException e) {
      log(e, AspectUtil
          .getNames(pjp), AspectUtil.getArgs(pjp));
      return dealListParamException(pjp, e, handler);
    } catch (ListParamsException e) {
      log(e, AspectUtil
          .getNames(pjp), AspectUtil.getArgs(pjp));
      return dealListParamsException(pjp, e, handler);
    } catch (Exception e) {
      log(e, AspectUtil
          .getNames(pjp), AspectUtil.getArgs(pjp));
      return handler.getErrRqRes(ErrorCode.UNEXPECTED_ERROR, e);
    }
  }

  private void log(Exception e, String[] paramNames, Object[] paramArgs) {
    log.warn("paramNames = " + Arrays.toString(paramNames)
        + ", paramArgs = " + Arrays.toString(paramArgs)
        + ", e = " + e + ", errMsg = " + e.getMessage()
        + ", stackTrace = " + Arrays.toString(e.getStackTrace())
        + ", time = " + BaseUtil.getCurrTime());
  }

  @SuppressWarnings("unchecked")
  private <T> RqResult<T> dealListParamException(
      ProceedingJoinPoint pjp, ListParamException e, RqResultHandler<T> handler) {
    Map<String, String> paramMap = AspectUtil.getParams(pjp);
    String transitionId = paramMap.get("requestId");

    List<ErrData> invalidData = new ArrayList<>();
    invalidData.add(new ErrData(e.getElementIdValue(), e.getMsg()));

    ErrListDataOut dataOut = new ErrListDataOut(transitionId, invalidData);
    return handler.getErrRqRes(ErrorCode.PARAMETER_ERROR, (T)JSONObject.toJSONString(dataOut));
  }

  @SuppressWarnings("unchecked")
  private <T> RqResult<T> dealListParamsException(ProceedingJoinPoint pjp, ListParamsException e, RqResultHandler<T> handler) {
    Map<String, String> paramsMap = AspectUtil.getParams(pjp);
    String transitionId = paramsMap.get("requestId");

    List<ErrData> invalidData = new ArrayList<>();
    for (ListParamException lpe : e.getListParamExceptions()) {
      invalidData.add(new ErrData(lpe.getElementIdValue(), lpe.getMsg()));
    }
    ErrListDataOut dataOut = new ErrListDataOut(transitionId, invalidData);

    return handler.getSuccessInvalidRqRes((T)dataOut);
  }


}
