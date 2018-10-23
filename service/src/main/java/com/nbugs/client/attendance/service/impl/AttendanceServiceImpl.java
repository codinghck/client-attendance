package com.nbugs.client.attendance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hck.util.base.BaseUtil;
import com.hck.util.http.HttpUtil;
import com.nbugs.client.attendance.dao.AttendanceDAO;
import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import com.nbugs.client.attendance.service.AttendanceService;
import com.nbugs.client.attendance.service.source.AttendanceSource;
import com.nbugs.client.attendance.service.source.OpenCenterSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 洪天才
 * @date 2018/10/23 10:23 AM client-attendance
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {
  private final AttendanceDAO attendanceDAO;
  private final OpenCenterSource openCenterSource;
  private final AttendanceSource attendanceSource;

  @Override
  public List<AttendanceDataDTO> getLocalAttendances() {
    return attendanceDAO.getAttendance();
  }

  @Override
  public void sendAttendanceMsg(List<AttendanceDataDTO> dataDTOS) {
    String accessToken = getAccessToken();
    String attendanceUrl = getAttendanceUrl(accessToken);
    String params = getTerminalAttendanceParams(dataDTOS);
    // TODO: 等正式使用的时候再加上这一行：HttpUtil.postJson(attendanceUrl, params)，不然呢会带来脏数据
    System.out.println("attendanceUrl:" + attendanceUrl + "params: " + params);
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

  private String getAccessToken() {
    Map<String, String> args = new HashMap<>(5);
    args.put("client_id", openCenterSource.getClientId());
    args.put("client_secret", openCenterSource.getClientSecret());
    args.put("grant_type", openCenterSource.getGrantType());
    args.put("response_type", openCenterSource.getResponseType());
    String res = HttpUtil.getMap(openCenterSource.getTokenUrl(), args);
    JSONObject jsonObject = JSONObject.parseObject(res);
    return jsonObject.getJSONObject("data").get("accessToken").toString();
  }

  private String getAttendanceUrl(String accessToken) {
    String terminalAttendanceUrl = attendanceSource.getTerminalAttendanceUrl();
    Map<String, String> args = new HashMap<>(1);
    args.put("access_token", accessToken);
    return HttpUtil.addMapToUrl(terminalAttendanceUrl, args);
  }

  @Autowired
  public AttendanceServiceImpl(
      AttendanceDAO attendanceDAO,
      OpenCenterSource openCenterSource,
      AttendanceSource attendanceSource) {
    this.attendanceDAO = attendanceDAO;
    this.openCenterSource = openCenterSource;
    this.attendanceSource = attendanceSource;
  }
}
