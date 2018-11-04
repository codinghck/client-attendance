package com.nbugs.client.attendance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hckisagoodboy.base.util.common.base.DateUtils;
import com.github.hckisagoodboy.base.util.common.base.LogUtils;
import com.github.hckisagoodboy.base.util.common.base.ObjUtils;
import com.github.hckisagoodboy.base.util.common.exception.ParamException;
import com.github.hckisagoodboy.base.util.common.http.HttpUtil;
import com.nbugs.client.attendance.service.OpenCenterService;
import com.nbugs.client.attendance.dao.source.OpenCenterSource;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hck
 * @date 2018/10/23 5:19 PM
 */
@Service
@Slf4j
public class OpenCenterServiceImpl implements OpenCenterService {
  private final OpenCenterSource source;
  private String token;
  private Long validTime;
  private Long lastTime;

  @Override
  public String getAccessToken() {
    if (isTokenExpired()) {
      Map<String, String> args = getTokenParams();
      String res = HttpUtil.getMap(source.getTokenUrl(), args);
      saveTokenRes(res);
    }
    return this.token;
  }

  private boolean isTokenExpired() {
    if (ObjUtils.hasNull(this.token, this.validTime, this.lastTime)) {
      return true;
    }
    return DateUtils.currentTimeSeconds() - this.lastTime > this.validTime;
  }

  private Map<String,String> getTokenParams() {
    Map<String, String> args = new HashMap<>(5);
    args.put("client_id", source.getClientId());
    args.put("client_secret", source.getClientSecret());
    args.put("grant_type", source.getGrantType());
    args.put("response_type", source.getResponseType());
    return args;
  }

  private void saveTokenRes(String reqRes) {
    JSONObject jsonObject = JSONObject.parseObject(reqRes);
    this.token = jsonObject.getJSONObject("data").get("accessToken").toString();
    this.validTime = getTokenExpireTime(reqRes);
    this.lastTime = System.currentTimeMillis() / 1000;
  }

  private Long getTokenExpireTime(String res) {
    JSONObject resJson = JSONObject.parseObject(res);
    JSONObject data = resJson.getJSONObject("data");
    return data.getLong("expiresIn");
  }

  @Override
  public JSONArray getLeaveTimes(String orgId, String userId, String time) {
    Map<String, String> params = getLeaveParams(orgId, userId, time);
    String reqRes = HttpUtil.getMap(source.getLeaveUrl(), params);
    log.info("params: {}, reqRes: {}", params, reqRes);
    return getTimesFromRes(reqRes);
  }

  private Map<String,String> getLeaveParams(String orgId, String userId, String time) {
    Map<String, String> params = new HashMap<>(4);
    params.put("access_token", getAccessToken());
    params.put("user_ids", userId);
    params.put("org_id", orgId);
    params.put("day_string", getFmtTime(time));
    return params;
  }

  private JSONArray getTimesFromRes(String reqRes) {
    try {
      JSONObject leave = JSONObject.parseObject(reqRes).getJSONObject("data");
      JSONArray users = leave.getJSONArray("users");
      if (users.size() > 0) {
        Object userLeave = users.get(0);
        String userLeaveStr = JSONObject.toJSONString(userLeave);
        JSONObject userLeaveJsonObj = JSONObject.parseObject(userLeaveStr);
        return userLeaveJsonObj.getJSONArray("leaveTimes");
      }
    } catch (Exception ignored) {}
    return null;
  }

  private String getFmtTime(String time) {
    try {
      return DateUtils.changeDateStr(time, "yyyy-MM-dd");
    } catch (ParseException e) {
      LogUtils.logErr(log, e);
      throw new ParamException("请检查日期格式!");
    }
  }

  @Autowired
  public OpenCenterServiceImpl(OpenCenterSource source) {
    this.source = source;
  }
}
