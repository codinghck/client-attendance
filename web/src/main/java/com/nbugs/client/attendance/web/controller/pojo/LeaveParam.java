package com.nbugs.client.attendance.web.controller.pojo;

import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author hongtiancai
 * @date 2018/10/30 1:14 PM
 */
@Data
public class LeaveParam {
  @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", message = "出生日期格式不正确")
  private String time;
  private String orgId;
  private String userId;
}
