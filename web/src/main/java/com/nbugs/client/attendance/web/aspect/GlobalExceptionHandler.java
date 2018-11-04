package com.nbugs.client.attendance.web.aspect;

import com.github.hckisagoodboy.base.util.common.exception.ParamException;
import com.github.hckisagoodboy.base.util.common.request.ErrCode;
import com.github.hckisagoodboy.base.util.common.request.ReqRes;
import com.github.hckisagoodboy.base.util.common.request.ResHandler;
import com.github.hckisagoodboy.base.util.common.util.LogUtils;
import java.util.List;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
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
    LogUtils.logErr(log, e);
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
    LogUtils.logErr(log, e);
    ResHandler<T> handler = new ResHandler<>();
    return handler.getByParamErr(e.getMessage());
  }

  @ExceptionHandler(value = ConstraintViolationException.class)
  public <T> ReqRes<T> constraintViolationExceptionHandler(ConstraintViolationException e) {
    LogUtils.logErr(log, e);
    ResHandler<T> handler = new ResHandler<>();
    return handler.getByParamErr(e.getMessage());
  }

  @ExceptionHandler(value = Exception.class)
  public <T> ReqRes<T> exceptionHandler(Exception e) {
    LogUtils.logErr(log, e);
    ResHandler<T> handler = new ResHandler<>();
    return handler.getRqResByCode(ErrCode.UNEXPECTED_ERROR, e.getMessage());
  }
}