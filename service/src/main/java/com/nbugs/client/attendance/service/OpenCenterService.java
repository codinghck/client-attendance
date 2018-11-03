package com.nbugs.client.attendance.service;

import com.alibaba.fastjson.JSONArray;

/**
 * @author hck
 * @date 2018/10/23 5:18 PM
 */
public interface OpenCenterService {
  /**
   * <p>获取开放平台的 token</p>
   *
   * @return 开放平台的 token 字符串
   */
  String getAccessToken();

  /**
   * <p>获取开放平台的 {@code orgId} 组织下的
   * 用户 {@code userId} 在 {@code time} 时间所在当天的所有请假时间</p>
   *
   * @return 所有请假时间的 <code>JSONArray</code> 对象
   */
  JSONArray getLeaveTimes(String orgId, String userId, String time);
}
