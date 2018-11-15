package com.nbugs.client.attendance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hckisagoodboy.base.util.common.base.JsonUtils;
import com.github.hckisagoodboy.base.util.common.base.LogUtils;
import com.github.hckisagoodboy.base.util.common.base.UUIDUtils;
import com.github.hckisagoodboy.base.util.common.http.HttpUtil;
import com.nbugs.client.attendance.dao.DeptDAO;
import com.nbugs.client.attendance.dao.pojo.DeptDataDTO;
import com.nbugs.client.attendance.service.DeptService;
import com.nbugs.client.attendance.service.OpenCenterService;
import com.nbugs.client.attendance.dao.source.DeptSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hck
 * @date 2018/10/23 5:11 PM
 */
@Service
@Slf4j
public class DeptServiceImpl implements DeptService {
  private final DeptDAO deptDAO;
  private final OpenCenterService openCenterService;
  private final DeptSource deptSource;

  @Override
  public List<DeptDataDTO> getLocalDepts() {
    return deptDAO.getAttendance();
  }

  @Override
  public String sendDeptsToOpenCenter(List<DeptDataDTO> dataDTOS) {
    String accessToken = openCenterService.getAccessToken();
    String sendDeptsUrl = getSendDeptsUrl(accessToken);
    log.info("上传组织开始, accessToken = {}, sendDeptsUrl = {}", accessToken, sendDeptsUrl);
    String params = getSendDeptParams(dataDTOS);
    String res = null;
    try {
      res = HttpUtil.postJson(sendDeptsUrl, params);
      log.info("上传组织开始, 请求结果 res = {}", res);
    } catch (IOException e) {
      LogUtils.logThrowable(log, e, "发送请求发生错误, 上传组织失败");
    }
    return res;
  }

  private String getSendDeptsUrl(String accessToken) {
    String sendDeptsUrl = deptSource.getSendDeptUrl();
    Map<String, String> args = new HashMap<>(1);
    args.put("access_token", accessToken);
    return HttpUtil.addMapToUrl(sendDeptsUrl, args);
  }

  private String getSendDeptParams(List<DeptDataDTO> datas) {
    JSONObject json = new JSONObject();
    JSONArray dataArr = new JSONArray();
    String transactionId = UUIDUtils.getUUID32();
    datas.iterator().forEachRemaining(data -> dataArr.add(JsonUtils.objToJsonObj(data)));
    json.put("transactionId", transactionId);
    json.put("data", dataArr);
    log.info("上传组织任务开始, transactionId = {}, 共 {} 条 data 数据, 第 1 条 data = {}",
        transactionId, dataArr.size(), dataArr.size() > 0 ? dataArr.get(0) : "null");
    return json.toJSONString();
  }

  @Autowired
  public DeptServiceImpl(
      DeptDAO deptDAO, OpenCenterService openCenterService, DeptSource deptSource) {
    this.deptDAO = deptDAO;
    this.openCenterService = openCenterService;
    this.deptSource = deptSource;
  }
}
