package com.nbugs.client.attendance.dao.pojo;

import lombok.Data;

/**
 * @author 洪天才
 * @date 2018/10/23 4:45 PM
 */
@Data
public class DeptDataDTO {
  private String dataId;
  private String deptId;
  private String deptName;
  private String parentId;
}
