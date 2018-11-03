package com.nbugs.client.attendance.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongtiancai.base.util.common.constant.Const;
import com.hongtiancai.base.util.common.request.NormalOut;
import com.hongtiancai.base.util.common.request.Param;
import com.hongtiancai.base.util.common.request.ReqRes;
import com.hongtiancai.base.util.common.request.ResHandler;
import com.hongtiancai.base.util.common.utils.DateUtil;
import com.nbugs.client.attendance.service.OpenCenterService;
import com.nbugs.client.attendance.web.controller.pojo.LeaveParam;
import java.text.ParseException;
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

  @RequestMapping(value = "/is_on_leave", method = RequestMethod.POST)
  public ReqRes<NormalOut> isOnLeaveTime(@RequestBody @Valid Param<LeaveParam> param) {
    NormalOut<String> outData = new NormalOut<>(param.getRequestId(), Const.FALSE);
    JSONArray leaveTimes = openCenterService.getLeaveTimes(
        param.getData().getOrgId(), param.getData().getUserId(), param.getData().getTime());
    if (null != leaveTimes) {
      outData.setData(isInLeave(param.getData().getTime(), leaveTimes));
    }

    return new ResHandler<NormalOut>().getSuccessRqRes(outData);
  }

  private String isInLeave(String time, JSONArray leaveTimes) {
    for (Object obj : leaveTimes) {
      String objStr = JSONObject.toJSONString(obj);
      JSONObject leaveTime = JSONObject.parseObject(objStr);
      String start = leaveTime.getString("leaveTime");
      String end = leaveTime.getString("backTime");
      try {
        if (DateUtil.isInRange(time, start, end, Const.NORMAL_DATE_FMT)) {
          return Const.TRUE;
        }
      } catch (ParseException ignored) {}
    }
    return Const.FALSE;
  }

  @Autowired
  public LeaveController(OpenCenterService openCenterService) {
    this.openCenterService = openCenterService;
  }
}
