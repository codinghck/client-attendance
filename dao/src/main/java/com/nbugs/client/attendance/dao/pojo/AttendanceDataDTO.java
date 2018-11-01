package com.nbugs.client.attendance.dao.pojo;

import java.util.List;
import lombok.Data;

/**
 * @author hck
 * @date 2018/10/23 3:02 PM
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
