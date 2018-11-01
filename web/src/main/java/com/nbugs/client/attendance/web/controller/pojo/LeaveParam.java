package com.nbugs.client.attendance.web.controller.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * @author hck
 * @date 2018/10/30 1:14 PM
 */
@Data
public class LeaveParam {
  @Pattern(
    regexp = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s"
      + "+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$",
    message = "格式错误, 正确格式为: yyyy-MM-dd hh:mm:ss"
  )
  private String time;
  @NotBlank
  @Size(max = 32)
  private String orgId;
  @NotBlank
  @Size(max = 32)
  private String userId;
}