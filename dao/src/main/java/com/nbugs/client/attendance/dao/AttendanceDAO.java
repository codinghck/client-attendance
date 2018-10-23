package com.nbugs.client.attendance.dao;

import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
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
  @Value("${attendance.db.last-execute-id}")
  private String lastExecuteId;

  /**
   * 正式使用或测试的时候再把这个代码复制到TODO下面，因为没有测试数据库直接写相应的 sql
   * dataDTO.setDeviceId(rs.getString("device_id"));
   * dataDTO.setTerminalId(rs.getString("terminal_id"));
   * dataDTO.setTime(rs.getString("time"));
   * dataDTO.setBehavior(rs.getString("behavior"));
   * dataDTO.setPicUrls(Arrays.asList((String[]) rs.getArray("picUrls").getArray()));
   */
  public List<AttendanceDataDTO> getAttendance() {
    return attendanceJdbcTemp.query(attendanceSql, new Object[]{lastExecuteId}, (rs, rowNum) -> {
      AttendanceDataDTO dataDTO = new AttendanceDataDTO();
      dataDTO.setDataId(rs.getInt("id") + "");
      // TODO: 正式使用或测试的时候再取消注释
      return dataDTO;
    });
  }

  @Autowired
  public AttendanceDAO(
      @Qualifier("attendanceJdbcTemplate") JdbcTemplate attendanceJdbcTemp) {
    this.attendanceJdbcTemp = attendanceJdbcTemp;
  }
}
