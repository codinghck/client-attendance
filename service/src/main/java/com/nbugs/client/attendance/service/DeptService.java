package com.nbugs.client.attendance.service;

import com.nbugs.client.attendance.dao.pojo.DeptDataDTO;
import java.util.List;

/**
 * @author hck
 * @date 2018/10/23 4:44 PM
 */
public interface DeptService {
  /**
   * <p>获取本地数据库中组织的数据</p>
   *
   * @return 本地数据库中考勤的数据
   */
  List<DeptDataDTO> getLocalDepts();

  /**
   * <p>发送组织数据到开放平台接入中心</p>
   *
   * @param dataDTOS 需要上传的组织数据列表
   * @return 上传数据后，开放平台的返回结果
   */
  String sendDeptsToOpenCenter(List<DeptDataDTO> dataDTOS);
}
