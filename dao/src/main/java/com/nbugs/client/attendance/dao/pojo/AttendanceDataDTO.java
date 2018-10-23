package com.nbugs.client.attendance.dao.pojo;

import java.util.List;
import lombok.Data;

/**
 * @author 洪天才
 * @date 2018/10/23 3:02 PM client-attendance
 */
@Data
public class AttendanceDataDTO {
  private String dataId;
  private String time;
  private String deviceId;
  private String terminalId;
  private String behavior;
  private List<String> picUrls;
}
