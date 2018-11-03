package com.nbugs.client.attendance.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hongtiancai.base.util.common.http.HttpUtil;
import com.hongtiancai.base.util.common.utils.DateUtil;
import com.hongtiancai.base.util.common.utils.ObjUtil;
import com.nbugs.client.attendance.service.OpenCenterService;
import com.nbugs.client.attendance.dao.source.OpenCenterSource;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hck
 * @date 2018/10/23 5:19 PM
 */
@Service
public class OpenCenterServiceImpl implements OpenCenterService {
  private final OpenCenterSource openCenterSource;
  private String token;
  private Long validTime;
  private Long lastTime;

  @Override
  public String getAccessToken() {
    if (isExpired()) {
      Map<String, String> args = getTokenParams();
      String res = HttpUtil.getMap(openCenterSource.getTokenUrl(), args);
      saveTokenRes(res);
    }
    return this.token;
  }

  private boolean isExpired() {
    if (ObjUtil.hasObjsNull(this.token, this.validTime, this.lastTime)) {
      return true;
    }
    return DateUtil.currentTimeSeconds() - this.lastTime > this.validTime;
  }

  private Map<String,String> getTokenParams() {
    Map<String, String> args = new HashMap<>(5);
    args.put("client_id", openCenterSource.getClientId());
    args.put("client_secret", openCenterSource.getClientSecret());
    args.put("grant_type", openCenterSource.getGrantType());
    args.put("response_type", openCenterSource.getResponseType());
    return args;
  }

  private void saveTokenRes(String reqRes) {
    JSONObject jsonObject = JSONObject.parseObject(reqRes);
    this.token = jsonObject.getJSONObject("data").get("accessToken").toString();
    this.validTime = getValidTime(reqRes);
    this.lastTime = System.currentTimeMillis() / 1000;
  }

  private Long getValidTime(String res) {
    JSONObject resJson = JSONObject.parseObject(res);
    JSONObject data = resJson.getJSONObject("data");
    return data.getLong("expiresIn");
  }

  @Autowired
  public OpenCenterServiceImpl(OpenCenterSource openCenterSource) {
    this.openCenterSource = openCenterSource;
  }
}
