package com.nbugs.client.attendance.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hck.util.http.HttpUtil;
import com.nbugs.client.attendance.service.OpenCenterService;
import com.nbugs.client.attendance.service.source.OpenCenterSource;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 洪天才
 * @date 2018/10/23 5:19 PM
 */
@Service
public class OpenCenterServiceImpl implements OpenCenterService {
  private final OpenCenterSource openCenterSource;

  @Override
  public String getAccessToken() {
    Map<String, String> args = new HashMap<>(5);
    args.put("client_id", openCenterSource.getClientId());
    args.put("client_secret", openCenterSource.getClientSecret());
    args.put("grant_type", openCenterSource.getGrantType());
    args.put("response_type", openCenterSource.getResponseType());
    String res = HttpUtil.getMap(openCenterSource.getTokenUrl(), args);
    JSONObject jsonObject = JSONObject.parseObject(res);
    return jsonObject.getJSONObject("data").get("accessToken").toString();
  }

  @Autowired
  public OpenCenterServiceImpl(OpenCenterSource openCenterSource) {
    this.openCenterSource = openCenterSource;
  }
}
