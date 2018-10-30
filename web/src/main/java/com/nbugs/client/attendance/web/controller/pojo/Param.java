package com.nbugs.client.attendance.web.controller.pojo;

import javax.validation.Valid;
import lombok.Data;

/**
 * @author hongtiancai
 * @date 2018/10/30 1:34 PM
 */
@Data
public class Param<T> {
  private String requestId;
  @Valid
  private T data;
}
