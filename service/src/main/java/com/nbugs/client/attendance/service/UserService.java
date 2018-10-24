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
  public List<UserDataDTO> getLocalUsers();

  /**
   * 发送用户数据到开放平台接入中心
   */
  public String sendUsersToOpenCenter(List<UserDataDTO> dataDTOS);
}
