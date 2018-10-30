package com.nbugs.client.attendance.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongtiancai.base.util.common.base.DateUtil;
import com.hongtiancai.base.util.common.base.LogUtil;
import com.hongtiancai.base.util.common.http.HttpUtil;
import com.hongtiancai.base.util.validation.Date;
import com.hongtiancai.base.util.validation.NotEmpty;
import com.hongtiancai.base.util.validation.Size;
import com.hongtiancai.base.util.validation.aspect.LogHandler;
import com.hongtiancai.base.util.validation.exception.ValidateException;
import com.hongtiancai.base.util.validation.pojo.NormalDataOut;
import com.hongtiancai.base.util.validation.request.RqResult;
import com.hongtiancai.base.util.validation.request.RqResultHandler;
import com.nbugs.client.attendance.dao.source.OpenCenterSource;
import com.nbugs.client.attendance.service.OpenCenterService;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hongtiancai
 * @date 2018/10/26 6:09 PM
 */
@RestController
@RequestMapping("/leave")
@Slf4j
public class LeaveController {

  private final OpenCenterService openCenterService;
  private final OpenCenterSource source;

  @RequestMapping(value = "/is_on_leave", method = RequestMethod.GET)
  public RqResult<NormalDataOut> isOnLeaveTime(
      @NotEmpty @RequestParam("requestId") @Size(max = 20) String requestId,
      @NotEmpty @RequestParam("time") @Date String time,
      @NotEmpty @RequestParam("orgId") @Size(max = 30) String orgId,
      @NotEmpty @RequestParam("userId") String userId) {
    NormalDataOut<String> outData = new NormalDataOut<>(requestId, "0");

    JSONArray leaveTimes = reqForLeaveTimes(userId, orgId, time);
    if (null != leaveTimes) {
      outData.setData(isInLeave(time, leaveTimes) ? "0" : "1");
    }

    return new RqResultHandler<NormalDataOut>().getSuccessInvalidRqRes(outData);
  }

  private JSONArray reqForLeaveTimes(String userId, String orgId, String time) {
    Map<String, String> params = getParams(userId, orgId, time);
    String reqRes = HttpUtil.getMap(source.getLeaveUrl(), params);
    log.info("请求开放平台是否请假时间参数为: {}, 返回结果为: {}", params, reqRes);
    return getLeaveTimes(reqRes);
  }

  private JSONArray getLeaveTimes(String reqRes) {
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

  private Map<String, String> getParams(
      String userId, String orgId, String time) throws ValidateException {
    Map<String, String> params = new HashMap<>(4);
    params.put("access_token", openCenterService.getAccessToken());
    params.put("user_ids", userId);
    params.put("org_id", orgId);
    params.put("day_string", getFormatTime(time));
    return params;
  }

  private boolean isInLeave(String time, JSONArray leaveTimes) {
    for (Object obj : leaveTimes) {
      String objStr = JSONObject.toJSONString(obj);
      JSONObject leaveTime = JSONObject.parseObject(objStr);
      String start = leaveTime.getString("leaveTime");
      String end = leaveTime.getString("backTime");
      if (DateUtil.isInRange(time, start, end)) {
        return true;
      }
    }
    return false;
  }

  private String getFormatTime(String time) {
    try {
      return DateUtil.changeDateStrByPattern(time, "yyyy-MM-dd");
    } catch (ParseException e) {
      LogUtil.logErr(log, e);
      throw new ValidateException("请检查日期格式!");
    }
  }

  @Autowired
  public LeaveController(
      OpenCenterService openCenterService,
      OpenCenterSource openCenterSource) {
    this.openCenterService = openCenterService;
    this.source = openCenterSource;
  }
}
