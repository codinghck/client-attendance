package com.nbugs.client.attendance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hck.util.base.BaseUtil;
import com.hck.util.http.HttpUtil;
import com.nbugs.client.attendance.dao.UserDAO;
import com.nbugs.client.attendance.dao.pojo.UserDataDTO;
import com.nbugs.client.attendance.service.OpenCenterService;
import com.nbugs.client.attendance.service.UserService;
import com.nbugs.client.attendance.dao.source.UserSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 洪天才
 * @date 2018/10/23 6:16 PM
 */
@Service
public class UserServiceImpl implements UserService {
  private final UserDAO userDAO;
  private final OpenCenterService openCenterService;
  private final UserSource userSource;

  @Override
  public List<UserDataDTO> getLocalUsers() {
    return userDAO.getUsers();
  }

  @Override
  public String sendUsersToOpenCenter(List<UserDataDTO> dataDTOS) {
    String accessToken = openCenterService.getAccessToken();
    String sendUsersUrl = getSendUsersUrl(accessToken);
    String params = getSendUserParams(dataDTOS);
    return HttpUtil.postJson(sendUsersUrl, params);
  }

  private String getSendUsersUrl(String accessToken) {
    String sendUsersUrl = userSource.getSendUserUrl();
    Map<String, String> args = new HashMap<>(1);
    args.put("access_token", accessToken);
    return HttpUtil.addMapToUrl(sendUsersUrl, args);
  }

  private String getSendUserParams(List<UserDataDTO> dataDTOS) {
    JSONObject json = new JSONObject();
    JSONArray dataArr = new JSONArray();
    for (UserDataDTO data : dataDTOS) {
      dataArr.add(JSONObject.parseObject(JSONObject.toJSONString(data)));
    }
    json.put("transactionId", BaseUtil.getUUID32());
    json.put("data", dataArr);
    return json.toJSONString();
  }

  @Autowired
  public UserServiceImpl(
      UserDAO userDAO,
      OpenCenterService openCenterService,
      UserSource userSource) {
    this.userDAO = userDAO;
    this.openCenterService = openCenterService;
    this.userSource = userSource;
  }
}
