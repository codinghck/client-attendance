package com.nbugs.client.attendance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hckisagoodboy.base.util.common.base.JsonUtils;
import com.github.hckisagoodboy.base.util.common.base.LogUtils;
import com.github.hckisagoodboy.base.util.common.base.UUIDUtils;
import com.github.hckisagoodboy.base.util.common.http.HttpUtil;
import com.nbugs.client.attendance.dao.AttendanceDAO;
import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import com.nbugs.client.attendance.service.AttendanceService;
import com.nbugs.client.attendance.service.OpenCenterService;
import com.nbugs.client.attendance.dao.source.AttendanceSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hck
 * @date 2018/10/23 10:23 AM
 */
@Service
@Slf4j
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
    log.info("上传考勤开始, accessToken = {}, attendanceUrl = {}", accessToken, attendanceUrl);
    String params = getTerminalAttendanceParams(dataDTOS);
    String res = null;
    try {
      res = HttpUtil.postJson(attendanceUrl, params);
      log.info("上传考勤开始, 请求结果 res = {}", res);
    } catch (IOException e) {
      LogUtils.logThrowable(log, e, "发送请求发生错误, 上传考勤失败");
    }
    return res;
  }

  private String getAttendanceUrl(String accessToken) {
    String terminalAttendanceUrl = attendanceSource.getTerminalAttendanceUrl();
    Map<String, String> args = new HashMap<>(1);
    args.put("access_token", accessToken);
    return HttpUtil.addMapToUrl(terminalAttendanceUrl, args);
  }

  private String getTerminalAttendanceParams(List<AttendanceDataDTO> datas) {
    JSONObject json = new JSONObject();
    JSONArray dataArr = new JSONArray();
    datas.iterator().forEachRemaining(data -> dataArr.add(JsonUtils.objToJsonObj(data)));
    String transactionId = UUIDUtils.getUUID32();
    json.put("transactionId", transactionId);
    json.put("data", dataArr);
    log.info("上传考勤任务开始, transactionId = {}, 共 {} 条 data 数据, 第 1 条 data = {}",
        transactionId, dataArr.size(), dataArr.size() > 0 ? dataArr.get(0) : "null");
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
