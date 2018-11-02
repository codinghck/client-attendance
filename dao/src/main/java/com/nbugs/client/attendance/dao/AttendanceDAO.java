package com.nbugs.client.attendance.dao;

import com.hongtiancai.base.util.common.base.BaseUtil;
import com.hongtiancai.base.util.common.base.LogUtil;
import com.hongtiancai.base.util.common.exception.UnExpectedException;
import com.hongtiancai.base.util.common.utils.PropertiesUtil;
import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import com.nbugs.client.attendance.dao.source.AttendanceSource;
import com.nbugs.client.attendance.dao.util.PropsUtil;
import com.nbugs.client.attendance.dao.util.Util;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.support.PropertiesLoaderUtils;
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
  private final static String EXECUTE_FILE_NULL = "数据执行位置文件不存在, 请参考 {} 配置文件中的 {} 项";
  private final static String MAP_FILE_NULL = "deviceId 映射文件不存在, 请参考 {} 配置文件中的 {} 项";

  public List<AttendanceDataDTO> getAttendance() {
    if (!isFileExist()) {
      return null;
    }
    List<AttendanceDataDTO> res = attendanceJdbcTemp.query(
        source.getAttendanceSql(), new Object[]{getLastId()}, (rs, rowNum) -> getDtoFromRs(rs));
    setLastId(res);
    return res;
  }

  @SneakyThrows({ConfigurationException.class, UnExpectedException.class})
  private void setLastId(List<AttendanceDataDTO> res) {
    if (null != res && res.size() > 0) {
      String lastId = res.get(res.size() - 1).getDataId();
      PropertiesUtil.setFirstValue(source.getExecutePositionFile(), lastId);
    }
  }

  @SneakyThrows({ConfigurationException.class})
  private String getLastId() {
    return PropertiesUtil.getFirstValue(source.getExecutePositionFile());
  }

  private AttendanceDataDTO getDtoFromRs(ResultSet rs) {
    Map<String, String> map = PropsUtil.getPropConf(source.getDeviceIdMap());
    String openCenterDeviceId = getOpenCenterDeviceId(map, Util.getByRs(rs, "device_id"));

    AttendanceDataDTO dataDTO = new AttendanceDataDTO();
    dataDTO.setDataId(Util.getByRs(rs, "id"));
    dataDTO.setDeviceId(openCenterDeviceId);
    dataDTO.setTerminalId(Util.getByRs(rs, "terminal_id"));
    dataDTO.setTime(Util.timeToSecond(Util.getByRs(rs, "time"), source.getTimeFormat()));
    dataDTO.setBehavior(Util.getByRs(rs, "behavior"));
    dataDTO.setPicUrls(Collections.singletonList(Util.getByRs(rs, "file_url")));
    return dataDTO;
  }

  private String getOpenCenterDeviceId(Map<String,String> map, String localDeviceId) {
    try {
      return map.get(localDeviceId);
    } catch (NullPointerException e) {
      LogUtil.logErr(log, e);
      return source.getOrgId();
    }
  }

  private boolean isFileExist() {
    return isExecuteFileExist() && isMapFileExist();
  }

  private boolean isExecuteFileExist() {
    String lastId;
    lastId = getLastId();
    if (BaseUtil.isStrNull(lastId)) {
      log.error(EXECUTE_FILE_NULL, "attendance.properties", "attendance.execute-position-file");
      return false;
    }
    return true;
  }

  private boolean isMapFileExist() {
    if (!new File(source.getDeviceIdMap()).exists()) {
      log.error(MAP_FILE_NULL, "attendance.properties", "attendance.device-id-map");
      return false;
    }
    return true;
  }

  @Autowired
  public AttendanceDAO(
      @Qualifier("attendanceJdbcTemplate") JdbcTemplate attendanceJdbcTemp,
      AttendanceSource attendanceSource) {
    this.attendanceJdbcTemp = attendanceJdbcTemp;
    this.source = attendanceSource;
  }
}
