package com.nbugs.client.attendance.service;

/**
 * @author hck
 * @date 2018/10/23 5:18 PM
 */
public interface OpenCenterService {
  /**
   * 获取开放平台的 token
   * @return 开放平台的 token 字符串
   */
  String getAccessToken();
}
