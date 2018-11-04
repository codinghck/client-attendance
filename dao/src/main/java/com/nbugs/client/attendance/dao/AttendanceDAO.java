package com.nbugs.client.attendance.dao;

import com.github.hckisagoodboy.base.util.common.base.DateUtils;
import com.github.hckisagoodboy.base.util.common.base.ListUtils;
import com.github.hckisagoodboy.base.util.common.base.LogUtils;
import com.github.hckisagoodboy.base.util.common.base.PropertiesUtils;
import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import com.nbugs.client.attendance.dao.source.AttendanceSource;
import com.nbugs.client.attendance.dao.util.Util;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author hck
 * @date 2018/10/22 11:25 PM
 */
@Repository
@Slf4j
public class AttendanceDAO {

  private final JdbcTemplate attendanceJdbcTemp;
  private final AttendanceSource source;

  public List<AttendanceDataDTO> getAttendance() {
    List<AttendanceDataDTO> res = attendanceJdbcTemp.query(
        source.getAttendanceSql(), new Object[]{getLastId()}, (rs, rowNum) -> getDtoFromRs(rs));
    setLastId(res);
    return res;
  }

  @SneakyThrows({ConfigurationException.class})
  private String getLastId() {
    return PropertiesUtils.getFirstValue(source.getExecutePositionFile());
  }

  @SneakyThrows({ConfigurationException.class})
  private void setLastId(List<AttendanceDataDTO> res) {
    if (!ListUtils.isEmpty(res)) {
      String lastId = res.get(res.size() - 1).getDataId();
      PropertiesUtils.setFirstValue(source.getExecutePositionFile(), lastId);
    }
  }

  @SneakyThrows({ConfigurationException.class})
  private AttendanceDataDTO getDtoFromRs(ResultSet rs) {
    AttendanceDataDTO dataDTO = new AttendanceDataDTO();
    dataDTO.setDataId(Util.getByRs(rs, "id"));
    dataDTO.setDeviceId(getMapDeviceId(Util.getByRs(rs, "device_id")));
    dataDTO.setTerminalId(Util.getByRs(rs, "terminal_id"));
    dataDTO.setTime(getSecondStr(rs));
    dataDTO.setBehavior(Util.getByRs(rs, "behavior"));
    dataDTO.setPicUrls(Collections.singletonList(Util.getByRs(rs, "file_url")));
    return dataDTO;
  }

  private String getSecondStr(ResultSet rs) {
    try {
      long s = DateUtils.dateStrToSecond(
          Util.getByRs(rs, "time"), source.getTimeFormat());
      return String.valueOf(s);
    } catch (ParseException e) {
      LogUtils.logErr(log, e);
      return "";
    }
  }

  private String getMapDeviceId(String localId) throws ConfigurationException {
    Map<String, String> map = PropertiesUtils.getToMap(source.getDeviceIdMap());
    try {
      return map.get(localId);
    } catch (NullPointerException e) {
      LogUtils.logErr(log, e);
      return source.getOrgId();
    }
  }

  @Autowired
  public AttendanceDAO(
      @Qualifier("attendanceJdbcTemplate") JdbcTemplate attendanceJdbcTemp,
      AttendanceSource attendanceSource) {
    this.attendanceJdbcTemp = attendanceJdbcTemp;
    this.source = attendanceSource;
  }
}
