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
  @Value("${attendance.db.time.format}")
  private String dataPattern;
  @Value("${attendance.local-dir}")
  private String localDir;

  public List<AttendanceDataDTO> getAttendance() {
    String lastId = PropsUtil.getProp(localDir + "attendance.properties", "attendance.db.last-execute-id");
    List<AttendanceDataDTO> res =  attendanceJdbcTemp.query(attendanceSql, new Object[]{null == lastId ? 0 : lastId}, (rs, rowNum) -> {
      AttendanceDataDTO dataDTO = new AttendanceDataDTO();
      dataDTO.setDataId(rs.getInt("id") + "");
      dataDTO.setDeviceId(Util.getByRs(rs, "device_id"));
      dataDTO.setTerminalId(Util.getByRs(rs, "terminal_id"));
      dataDTO.setTime(Util.timeToMills(Util.getByRs(rs, "time"), dataPattern));
      dataDTO.setBehavior(Util.getByRs(rs, "behavior"));
      dataDTO.setPicUrls(Collections.singletonList(Util.getByRs(rs, "file_url")));
      return dataDTO;
    });
    if (res.size() > 0) {
      PropsUtil.setProp(localDir + "attendance.properties", "attendance.db.last-execute-id", res.get(res.size() - 1).getDataId());
    }
    return res;
  }

  @Autowired
  public AttendanceDAO(
      @Qualifier("attendanceJdbcTemplate") JdbcTemplate attendanceJdbcTemp) {
    this.attendanceJdbcTemp = attendanceJdbcTemp;
  }
}
