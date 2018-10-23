package com.nbugs.client.attendance.service;

import com.nbugs.client.attendance.dao.pojo.DeptDataDTO;
import java.util.List;

/**
 * @author 洪天才
 * @date 2018/10/23 4:44 PM
 */
public interface DeptService {
  /**
   * 获取本地数据库中组织的数据
   * @return 本地数据库中考勤的数据
   */
  public List<DeptDataDTO> getLocalDepts();

  /**
   * 发送组织数据到开放平台接入中心
   */
  public void sendDeptsToOpenCenter(List<DeptDataDTO> dataDTOS);
}
