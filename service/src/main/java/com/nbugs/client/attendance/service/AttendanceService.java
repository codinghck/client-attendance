package com.nbugs.client.attendance.service;

import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import java.util.List;

/**
 * @author 洪天才
 * @date 2018/10/23 10:22 AM
 */
public interface AttendanceService {
  /**
   * 获取本地数据库中考勤的数据
   * @return 本地数据库中考勤的数据
   */
  List<AttendanceDataDTO> getLocalAttendances();

  /**
   * 发送考勤数据到开放平台接入中心
   * @param dataDTOS 考勤数据列表
   * @return 发送结果
   */
  String sendAttendanceMsg(List<AttendanceDataDTO> dataDTOS);
}
