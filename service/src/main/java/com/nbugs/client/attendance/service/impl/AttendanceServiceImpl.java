package com.nbugs.client.attendance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hck.util.base.BaseUtil;
import com.hck.util.http.HttpUtil;
import com.nbugs.client.attendance.dao.AttendanceDAO;
import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import com.nbugs.client.attendance.service.AttendanceService;
import com.nbugs.client.attendance.service.OpenCenterService;
import com.nbugs.client.attendance.dao.source.AttendanceSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 洪天才
 * @date 2018/10/23 10:23 AM
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {
  private final AttendanceDAO attendanceDAO;
  private final OpenCenterService openCenterService;
  private final AttendanceSource attendanceSource;

  @Override
  public List<AttendanceDataDTO> getLocalAttendances() {
    return attendanceDAO.getAttendance();
  }

  @Override
  public String sendAttendanceMsg(List<AttendanceDataDTO> dataDTOS) {
    String accessToken = openCenterService.getAccessToken();
    String attendanceUrl = getAttendanceUrl(accessToken);
    String params = getTerminalAttendanceParams(dataDTOS);
    // TODO: 等正式使用的时候再加上这一行：return HttpUtil.postJson(attendanceUrl, params)，不然呢会带来脏数据
    return "";
  }

  private String getAttendanceUrl(String accessToken) {
    String terminalAttendanceUrl = attendanceSource.getTerminalAttendanceUrl();
    Map<String, String> args = new HashMap<>(1);
    args.put("access_token", accessToken);
    return HttpUtil.addMapToUrl(terminalAttendanceUrl, args);
  }

  private String getTerminalAttendanceParams(List<AttendanceDataDTO> dataDTOS) {
    JSONObject json = new JSONObject();
    JSONArray dataArr = new JSONArray();
    for (AttendanceDataDTO data : dataDTOS) {
      dataArr.add(JSONObject.parseObject(JSONObject.toJSONString(data)));
    }
    json.put("transactionId", BaseUtil.getUUID32());
    json.put("data", dataArr);
    return json.toJSONString();
  }

  @Autowired
  public AttendanceServiceImpl(
      AttendanceDAO attendanceDAO,
      OpenCenterService openCenterService,
      AttendanceSource attendanceSource) {
    this.attendanceDAO = attendanceDAO;
    this.openCenterService = openCenterService;
    this.attendanceSource = attendanceSource;
  }
}
