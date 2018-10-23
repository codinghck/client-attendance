package com.nbugs.client.attendance.service;

import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author 洪天才
 * @date 2018/10/23 10:22 AM client-attendance
 */
public interface AttendanceService {

  /**
   * 获取本地数据库中考勤的数据
   * @return 本地数据库中考勤的数据
   */
  public List<AttendanceDataDTO> getLocalAttendances();

  /**
   * 发送考勤数据到开放平台接入中心
   */
  public void sendAttendanceMsg(List<AttendanceDataDTO> dataDTOS);
}
