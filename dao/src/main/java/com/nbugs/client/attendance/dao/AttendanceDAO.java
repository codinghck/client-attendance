package com.nbugs.client.attendance.dao;

import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author 洪天才
 * @date 2018/10/22 11:25 PM client-attendance
 */
@Repository
@PropertySource("classpath:tasks/attendance.properties")
public class AttendanceDAO {
  private final JdbcTemplate attendanceJdbcTemp;

  @Value("${attendance.db.attendance-sql}")
  private String attendanceSql;
  @Value("${attendance.db.attendance-pics-sql}")
  private String attendancePicsSql;
  @Value("${attendance.db.last-execute-id}")
  private String lastExecuteId;
  @Value("${attendance.db.time.format}")
  private String dataPattern;

  public List<AttendanceDataDTO> getAttendance() {
    return attendanceJdbcTemp.query(attendanceSql, new Object[]{lastExecuteId}, (rs, rowNum) -> {
      AttendanceDataDTO dataDTO = new AttendanceDataDTO();
      dataDTO.setDataId(rs.getInt("id") + "");
      dataDTO.setDeviceId(Util.getByRs(rs, "device_id"));
      dataDTO.setTerminalId(Util.getByRs(rs, "terminal_id"));
      dataDTO.setTime(Util.timeToMills(Util.getByRs(rs, "time"), dataPattern));
      dataDTO.setBehavior(Util.getByRs(rs, "behavior"));
      dataDTO.setPicUrls(Collections.singletonList(Util.getByRs(rs, "file_url")));
      return dataDTO;
    });
  }

  @Autowired
  public AttendanceDAO(
      @Qualifier("attendanceJdbcTemplate") JdbcTemplate attendanceJdbcTemp) {
    this.attendanceJdbcTemp = attendanceJdbcTemp;
  }
}
