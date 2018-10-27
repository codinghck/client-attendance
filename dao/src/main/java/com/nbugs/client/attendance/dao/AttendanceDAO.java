package com.nbugs.client.attendance.dao;

import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import com.nbugs.client.attendance.dao.source.AttendanceSource;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author 洪天才
 * @date 2018/10/22 11:25 PM client-attendance
 */
@Repository
public class AttendanceDAO {
  private final JdbcTemplate attendanceJdbcTemp;
  private final AttendanceSource source;

  public List<AttendanceDataDTO> getAttendance() {
    String lastId = PropsUtil.getProp(source.getLocalDir() + "attendance.properties", "attendance.db.last-execute-id");
    List<AttendanceDataDTO> res =  attendanceJdbcTemp.query(source.getAttendanceSql(), new Object[]{null == lastId ? 0 : lastId}, (rs, rowNum) -> {
      AttendanceDataDTO dataDTO = new AttendanceDataDTO();
      dataDTO.setDataId(rs.getInt("id") + "");
      dataDTO.setDeviceId(Util.getByRs(rs, "device_id"));
      dataDTO.setTerminalId(Util.getByRs(rs, "terminal_id"));
      dataDTO.setTime(Util.timeToMills(Util.getByRs(rs, "time"), source.getTimeFormat()));
      dataDTO.setBehavior(Util.getByRs(rs, "behavior"));
      dataDTO.setPicUrls(Collections.singletonList(Util.getByRs(rs, "file_url")));
      return dataDTO;
    });
    if (res.size() > 0) {
      PropsUtil.setProp(source.getLocalDir() + "attendance.properties", "attendance.db.last-execute-id", res.get(res.size() - 1).getDataId());
    }
    return res;
  }

  @Autowired
  public AttendanceDAO(
      @Qualifier("attendanceJdbcTemplate") JdbcTemplate attendanceJdbcTemp,
      AttendanceSource attendanceSource) {
    this.attendanceJdbcTemp = attendanceJdbcTemp;
    this.source = attendanceSource;
  }
}
