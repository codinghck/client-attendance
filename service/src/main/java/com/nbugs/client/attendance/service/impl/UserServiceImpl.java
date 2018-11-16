package com.nbugs.client.attendance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hckisagoodboy.base.util.common.base.JsonUtils;
import com.github.hckisagoodboy.base.util.common.base.LogUtils;
import com.github.hckisagoodboy.base.util.common.base.UUIDUtils;
import com.github.hckisagoodboy.base.util.common.http.HttpUtil;
import com.nbugs.client.attendance.dao.UserDAO;
import com.nbugs.client.attendance.dao.pojo.UserDataDTO;
import com.nbugs.client.attendance.service.OpenCenterService;
import com.nbugs.client.attendance.service.UserService;
import com.nbugs.client.attendance.dao.source.UserSource;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hck
 * @date 2018/10/23 6:16 PM
 */
@Service
@Slf4j
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
    log.info("上传用户开始, accessToken = {}, sendUsersUrl = {}", accessToken, sendUsersUrl);
    String params = getSendUserParams(dataDTOS);
    String res = null;
    try {
      res = HttpUtil.postJson(sendUsersUrl, params);
      log.info("上传用户开始, 请求结果 res = {}", res);
    } catch (IOException e) {
      LogUtils.logThrowable(log, e, "发送请求发生错误, 上传用户失败");
    }
    return res;
  }

  private String getSendUsersUrl(String accessToken) {
    return userSource.getSendUserUrl() + "?access_token=" + accessToken;
  }

  private String getSendUserParams(List<UserDataDTO> datas) {
    JSONArray jsonArr = new JSONArray();
    datas.iterator().forEachRemaining(data -> jsonArr.add(JsonUtils.objToJsonObj(data)));
    JSONObject params = new JSONObject();
    String uuid32 = UUIDUtils.getUUID32();
    params.put("transactionId", uuid32);
    params.put("data", jsonArr);
    log.info("上传用户任务开始, transactionId = {}, 共 {} 条 data 数据, 第 1 条 data = {}",
        uuid32, jsonArr.size(), jsonArr.size() > 0 ? jsonArr.get(0) : "null");
    return params.toJSONString();
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
