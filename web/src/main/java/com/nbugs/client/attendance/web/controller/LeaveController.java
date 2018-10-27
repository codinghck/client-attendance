package com.nbugs.client.attendance.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hck.util.http.HttpUtil;
import com.hck.util.request.RqResult;
import com.hck.util.request.RqResultHandler;
import com.hck.util.validate.Date;
import com.hck.util.validate.JsonArrayStr;
import com.hck.util.validate.NotEmpty;
import com.hck.util.validate.Size;
import com.hck.util.validate.pojo.NormalDataOut;
import com.nbugs.client.attendance.service.OpenCenterService;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hongtiancai create: 2018/10/26 6:09 PM
 */
@RestController
@RequestMapping("/leave")
@Slf4j
public class LeaveController {
  private static final String OPEN_CENTER_LEAVE_URL = "https://open.api.xiaoyuanhao.com/leave/leave_times/batchget";
  private final OpenCenterService openCenterService;

  @RequestMapping(value = "/is_on_leave", method = RequestMethod.GET)
  public RqResult<NormalDataOut> isOnLeaveTime(
      @NotEmpty @Size(max = 20) @RequestParam("requestId") String requestId,
      @NotEmpty @Date(fmtPattern = "yyyy-MM-dd hh:mm:ss") @RequestParam("time") String time,
      @NotEmpty @Size(max = 30) @RequestParam("orgId") String orgId,
      @NotEmpty @RequestParam("userId") String userId) {
    RqResultHandler<NormalDataOut> handler = new RqResultHandler<>();

    String res = "0";

    Map<String, String> params = new HashMap<>(4);
    params.put("access_token", openCenterService.getAccessToken());
    try {
      params.put("day_string", new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(time)));
    } catch (ParseException e) {
      log.error("prop err, e = {}, msg = {}, stackTrace = {}",
          e, e.getMessage(), e.getStackTrace());
    }
    params.put("user_ids", userId);
    params.put("org_id", orgId);
    String reqRes = HttpUtil.getMap(OPEN_CENTER_LEAVE_URL, params);


      JSONArray userLeaveDatas = JSONObject.parseObject(reqRes).getJSONObject("data").getJSONArray("users");
      if (userLeaveDatas.size() > 0) {
        try {
          Object userLeaveData = userLeaveDatas.get(0);
          JSONArray leaveTimes = JSONObject.parseObject(JSONObject.toJSONString(userLeaveData)).getJSONArray("leaveTimes");

          for (Object obj : leaveTimes) {
            JSONObject leaveTime = JSONObject.parseObject(JSONObject.toJSONString(obj));
            String dateStart = leaveTime.getString("leaveTime");
            String dateEnd = leaveTime.getString("backTime");
            if (compare(dateStart, time) == -1 || compare(dateEnd, time) == 1) {
              res = "0";
            } else {
              res = "1";
            }
          }
        } catch (Exception e) {
          log.error("prop err, e = {}, msg = {}, stackTrace = {}",
              e, e.getMessage(), e.getStackTrace());
        }
      }



    return handler.getSuccessInvalidRqRes(new NormalDataOut<>(requestId, res));
  }

  private static int compare(String date1, String date2) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    try {
      java.util.Date dt1 = df.parse(date1);
      java.util.Date dt2 = df.parse(date2);
      if (dt1.getTime() > dt2.getTime()) {
        // "dt1 在 dt2 前"
        return 1;
      } else if (dt1.getTime() <dt2.getTime()) {
        // "dt1 在 dt2 后"
        return -1;
      } else {
        return 0;
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return 0;
  }


  @Autowired
  public LeaveController(OpenCenterService openCenterService) {
    this.openCenterService = openCenterService;
  }
}
