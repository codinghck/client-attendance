package com.nbugs.client.attendance.dao;

import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import com.nbugs.client.attendance.dao.source.AttendanceSource;
import com.nbugs.client.attendance.dao.util.PropsUtil;
import com.nbugs.client.attendance.dao.util.Util;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author hck
 * @date 2018/10/22 11:25 PM
 */
@Repository
public class AttendanceDAO {
  private final JdbcTemplate attendanceJdbcTemp;
  private final AttendanceSource source;

  public List<AttendanceDataDTO> getAttendance() {
    List<AttendanceDataDTO> res =  attendanceJdbcTemp.query(
        source.getAttendanceSql(), new Object[]{getLastId()}, (rs, rowNum) -> getDtoFromRs(rs));
    setLastId(res);
    return res;
  }

  private void setLastId(List<AttendanceDataDTO> res) {
    if (null != res && res.size() > 0) {
      String path = source.getLocalDir() + "attendance.properties";
      String key = "attendance.last-execute-id";
      String lastId = res.get(res.size() - 1).getDataId();
      PropsUtil.setProp(path, key, lastId);
    }
  }

  private String getLastId() {
    String path = source.getLocalDir() + "attendance.properties";
    String key = "attendance.last-execute-id";
    String lastId = PropsUtil.getProp(path, key);
    return null == lastId ? "0" : lastId;
  }

  private AttendanceDataDTO getDtoFromRs(ResultSet rs) {
    AttendanceDataDTO dataDTO = new AttendanceDataDTO();
    dataDTO.setDataId(Util.getByRs(rs,"id"));
    dataDTO.setDeviceId(Util.getByRs(rs, "device_id"));
    dataDTO.setTerminalId(Util.getByRs(rs, "terminal_id"));
    dataDTO.setTime(Util.timeToMills(Util.getByRs(rs, "time"), source.getTimeFormat()));
    dataDTO.setBehavior(Util.getByRs(rs, "behavior"));
    dataDTO.setPicUrls(Collections.singletonList(Util.getByRs(rs, "file_url")));
    return dataDTO;
  }

  @Autowired
  public AttendanceDAO(
      @Qualifier("attendanceJdbcTemplate") JdbcTemplate attendanceJdbcTemp,
      AttendanceSource attendanceSource) {
    this.attendanceJdbcTemp = attendanceJdbcTemp;
    this.source = attendanceSource;
  }
}
