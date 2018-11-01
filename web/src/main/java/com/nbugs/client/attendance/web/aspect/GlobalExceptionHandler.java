package com.nbugs.client.attendance.web.aspect;

import com.hongtiancai.base.util.common.base.LogUtil;
import com.hongtiancai.base.util.common.exception.ParamException;
import com.hongtiancai.base.util.common.request.ErrCode;
import com.hongtiancai.base.util.common.request.ReqRes;
import com.hongtiancai.base.util.common.request.ResHandler;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author hck
 * @date 2018/10/30 1:52 PM
 */
@RestControllerAdvice
@Slf4j
@Order(10)
public class GlobalExceptionHandler {
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public <T> ReqRes<T> exception(MethodArgumentNotValidException e) {
    LogUtil.logErr(log, e);
    BindingResult result = e.getBindingResult();
    final List<FieldError> fieldErrors = result.getFieldErrors();
    StringBuilder builder = new StringBuilder();

    for (FieldError error : fieldErrors) {
      builder.append("参数 ");
      builder.append(error.getField());
      builder.append(" ");
      builder.append(error.getDefaultMessage());
    }

    ResHandler<T> handler = new ResHandler<>();
    return handler.getByParamErr(builder.toString());
  }

  @ExceptionHandler(value = ParamException.class)
  public <T> ReqRes<T> paramExceptionHandler(ParamException e) {
    LogUtil.logErr(log, e);
    ResHandler<T> handler = new ResHandler<>();
    return handler.getByParamErr(e.getMessage());
  }

  @ExceptionHandler(value = ConstraintViolationException.class)
  public <T> ReqRes<T> constraintViolationExceptionHandler(ConstraintViolationException e) {
    LogUtil.logErr(log, e);
    ResHandler<T> handler = new ResHandler<>();
    return handler.getByParamErr(e.getMessage());
  }

  @ExceptionHandler(value = Exception.class)
  public <T> ReqRes<T> exceptionHandler(Exception e) {
    LogUtil.logErr(log, e);
    ResHandler<T> handler = new ResHandler<>();
    return handler.getRqResByCode(ErrCode.UNEXPECTED_ERROR, e.getMessage());
  }
}