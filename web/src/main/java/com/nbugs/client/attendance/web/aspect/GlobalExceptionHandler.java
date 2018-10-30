package com.nbugs.client.attendance.web.aspect;

import com.hongtiancai.base.util.validation.request.ErrorCode;
import com.hongtiancai.base.util.validation.request.RqResult;
import com.hongtiancai.base.util.validation.request.RqResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author hongtiancai
 * @date 2018/10/30 1:52 PM
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * 处理请求对象属性不满足校验规则的异常信息
   *
   * @param request
   * @param exception
   * @return
   * @throws Exception
   */
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public <T> RqResult<T> exception(HttpServletRequest request, MethodArgumentNotValidException exception) {
    BindingResult result = exception.getBindingResult();
    final List<FieldError> fieldErrors = result.getFieldErrors();
    StringBuilder builder = new StringBuilder();

    for (FieldError error : fieldErrors) {
      builder.append(error.getDefaultMessage() + "\n");
    }
//    return new ReturnMsg(ReturnEnum.FAIL, builder.toString());
    RqResultHandler<T> handler = new RqResultHandler<>();
    return handler.getErrRqRes(ErrorCode.UNEXPECTED_ERROR, builder.toString());
  }

  /**
   * 处理请求单个参数不满足校验规则的异常信息
   *
   * @param request
   * @param exception
   * @return
   * @throws Exception
   */
  @ExceptionHandler(value = ConstraintViolationException.class)
  public <T> RqResult<T> constraintViolationExceptionHandler(HttpServletRequest request, ConstraintViolationException exception) {
    logger.info(exception.getMessage());
//    return new ReturnMsg(ReturnEnum.FAIL, exception.getMessage());
    RqResultHandler<T> handler = new RqResultHandler<>();
    return handler.getErrRqRes(ErrorCode.UNEXPECTED_ERROR, exception.getMessage());
  }


  /**
   * 处理未定义的其他异常信息
   *
   * @param request
   * @param exception
   * @return
   */
  @ExceptionHandler(value = Exception.class)
  public <T> RqResult<T> exceptionHandler(HttpServletRequest request, Exception exception) {
    logger.error(exception.getMessage());
//    return new ReturnMsg(ReturnEnum.FAIL, exception.getMessage());
    RqResultHandler<T> handler = new RqResultHandler<>();
    return handler.getErrRqRes(ErrorCode.UNEXPECTED_ERROR, exception.getMessage());
  }
}