package com.nbugs.client.attendance.dao.pojo;

import lombok.Data;

/**
 * @author 洪天才
 * @date 2018/10/23 6:05 PM
 */
@Data
public class UserDataDTO {
  private String dataId;
  private String orgId;
  private String userId;
  private String userName;
  private String card;
  private String deptId;
  private String deptName;
}
