package com.nbugs.client.attendance.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongtiancai.base.util.common.base.DateUtil;
import com.hongtiancai.base.util.common.base.LogUtil;
import com.hongtiancai.base.util.common.constant.Const;
import com.hongtiancai.base.util.common.exception.ParamException;
import com.hongtiancai.base.util.common.http.HttpUtil;
import com.hongtiancai.base.util.common.request.NormalOut;
import com.hongtiancai.base.util.common.request.Param;
import com.hongtiancai.base.util.common.request.ReqRes;
import com.hongtiancai.base.util.common.request.ResHandler;
import com.nbugs.client.attendance.dao.source.OpenCenterSource;
import com.nbugs.client.attendance.service.OpenCenterService;
import com.nbugs.client.attendance.web.controller.pojo.LeaveParam;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hck
 * @date 2018/10/26 6:09 PM
 */
@RestController
@RequestMapping("/leave")
@Slf4j
public class LeaveController {

  private final OpenCenterService openCenterService;
  private final OpenCenterSource source;

  @RequestMapping(value = "/is_on_leave", method = RequestMethod.POST)
  public ReqRes<NormalOut> isOnLeaveTime(@RequestBody @Valid Param<LeaveParam> param) {
    NormalOut<String> outData = new NormalOut<>(param.getRequestId(), Const.FALSE);

    JSONArray leaveTimes = reqForLeaveTimes(param.getData());
    if (null != leaveTimes) {
      outData.setData(isInLeave(param.getData().getTime(), leaveTimes) ? Const.FALSE : Const.TRUE);
    }

    return new ResHandler<NormalOut>().getSuccessRqRes(outData);
  }

  private JSONArray reqForLeaveTimes(LeaveParam param) {
    Map<String, String> params = getParams(param.getUserId(), param.getOrgId(), param.getTime());
    String reqRes = HttpUtil.getMap(source.getLeaveUrl(), params);
    log.info("params: {}, reqRes: {}", params, reqRes);
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
      String userId, String orgId, String time) {
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
      throw new ParamException("请检查日期格式!");
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
