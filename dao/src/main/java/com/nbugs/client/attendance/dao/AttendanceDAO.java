package com.nbugs.client.attendance.dao;

import com.github.hckisagoodboy.base.util.common.base.DateUtils;
import com.github.hckisagoodboy.base.util.common.base.IteratorUtils;
import com.github.hckisagoodboy.base.util.common.base.ListUtils;
import com.github.hckisagoodboy.base.util.common.base.LogUtils;
import com.github.hckisagoodboy.base.util.common.base.PropertiesUtils;
import com.github.hckisagoodboy.base.util.common.base.StrUtils;
import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import com.nbugs.client.attendance.dao.source.AttendanceSource;
import com.nbugs.client.attendance.dao.util.Util;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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

  private final JdbcTemplate jdbcTemplate;
  private final AttendanceSource source;

  public List<AttendanceDataDTO> getAttendance() {
    List<AttendanceDataDTO> res = jdbcTemplate.query(
        source.getAttendanceSql(), new Object[]{getLastId()}, (rs, rowNum) -> getDtoFromRs(rs));
    setLastId(res);
    return res;
  }

  @SneakyThrows({ConfigurationException.class})
  private String getLastId() {
//    String lastId = PropertiesUtils.getFirstValue(source.getExecutePositionFile());
    String lastId = PropertiesUtils.load(source.getExecutePositionFile()).getString("attendance.last-execute-id");
    log.info("上次考勤执行位置为 {}", lastId);
    return lastId;
  }

  @SneakyThrows({ConfigurationException.class})
  private void setLastId(List<AttendanceDataDTO> res) {
    if (!ListUtils.isEmpty(res)) {
      String lastId = res.get(res.size() - 1).getDataId();
      PropertiesConfiguration props = PropertiesUtils.load(source.getExecutePositionFile());
      props.setAutoSave(true);
      props.setEncoding("UTF-8");
      Iterator<String> it = props.getKeys();
      String key = it.next();
      log.info("下次考勤执行开始位置为 {}, 需要设置的 key = {}", lastId, key);
      props.setProperty("attendance.last-execute-id", lastId);


//      PropertiesUtils.setFirstValue(source.getExecutePositionFile(), "attendance.last-execute-id", lastId);
    }
  }

  @SneakyThrows({ConfigurationException.class})
  private AttendanceDataDTO getDtoFromRs(ResultSet rs) {
    AttendanceDataDTO dataDTO = new AttendanceDataDTO();
    dataDTO.setDataId(Util.getByRs(rs, "id"));
    dataDTO.setDeviceId(getMapDeviceId(Util.getByRs(rs, "device_id")));
    dataDTO.setTime(getSecondStr(rs));
    dataDTO.setBehavior(Util.getByRs(rs, "behavior"));
    dataDTO.setPicUrls(Collections.singletonList(Util.getByRs(rs, "file_url")));
    String terminalId = Util.getByRs(rs, "terminal_id");
    if (terminalId != null) {
      terminalId = StrUtils.addLeftIfLenNotEnough(terminalId, '0', 10);
    }
    dataDTO.setTerminalId(terminalId);
    return dataDTO;
  }

  private String getSecondStr(ResultSet rs) {
    try {
      long s = DateUtils.dateStrToSecond(
          Util.getByRs(rs, "time"), source.getTimeFormat());
      return String.valueOf(s);
    } catch (ParseException e) {
      LogUtils.logThrowable(log, e);
      return "";
    }
  }

  private String getMapDeviceId(String localId) throws ConfigurationException {
    Map<String, String> map = PropertiesUtils.getToMap(source.getDeviceIdMap());
    try {
      return map.get(localId);
    } catch (NullPointerException e) {
      LogUtils.logThrowable(log, e);
      return source.getOrgId();
    }
  }

  @Autowired
  public AttendanceDAO(
      @Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate,
      AttendanceSource attendanceSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.source = attendanceSource;
  }
}
