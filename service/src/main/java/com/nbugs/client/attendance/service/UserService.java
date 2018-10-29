package com.nbugs.client.attendance.service;

import com.nbugs.client.attendance.dao.pojo.UserDataDTO;
import java.util.List;

/**
 * @author 洪天才
 * @date 2018/10/23 6:13 PM
 */
public interface UserService {
  /**
   * 获取本地数据库中用户的数据
   * @return 本地数据库中用户的数据
   */
  List<UserDataDTO> getLocalUsers();

  /**
   * 发送用户数据到开放平台接入中心
   * @param dataDTOS 需要上传到开放平台的用户数据列表
   * @return 开发平台返回的上传结果
   */
  String sendUsersToOpenCenter(List<UserDataDTO> dataDTOS);
}
