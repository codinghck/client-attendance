package com.nbugs.client.attendance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongtiancai.base.util.common.base.BaseUtil;
import com.hongtiancai.base.util.common.http.HttpUtil;
import com.nbugs.client.attendance.dao.DeptDAO;
import com.nbugs.client.attendance.dao.pojo.DeptDataDTO;
import com.nbugs.client.attendance.service.DeptService;
import com.nbugs.client.attendance.service.OpenCenterService;
import com.nbugs.client.attendance.dao.source.DeptSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 洪天才
 * @date 2018/10/23 5:11 PM
 */
@Service
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
    String params = getSendDeptParams(dataDTOS);
    return HttpUtil.postJson(sendDeptsUrl, params);
  }

  private String getSendDeptsUrl(String accessToken) {
    String sendDeptsUrl = deptSource.getSendDeptUrl();
    Map<String, String> args = new HashMap<>(1);
    args.put("access_token", accessToken);
//    return HttpUtil.addMapToUrl(sendDeptsUrl, args);
    return "";
  }

  private String getSendDeptParams(List<DeptDataDTO> dataDTOS) {
    JSONObject json = new JSONObject();
    JSONArray dataArr = new JSONArray();
    for (DeptDataDTO data : dataDTOS) {
      dataArr.add(JSONObject.parseObject(JSONObject.toJSONString(data)));
    }
    json.put("transactionId", BaseUtil.getUUID32());
    json.put("data", dataArr);
    return json.toJSONString();
  }

  @Autowired
  public DeptServiceImpl(
      DeptDAO deptDAO,
      OpenCenterService openCenterService,
      DeptSource deptSource) {
    this.deptDAO = deptDAO;
    this.openCenterService = openCenterService;
    this.deptSource = deptSource;
  }
}
