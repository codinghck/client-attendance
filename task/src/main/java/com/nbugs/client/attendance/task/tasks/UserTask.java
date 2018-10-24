package com.nbugs.client.attendance.task.tasks;

import com.nbugs.client.attendance.dao.pojo.UserDataDTO;
import com.nbugs.client.attendance.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 洪天才
 * @date 2018/10/22 4:06 PM client-attendance
 */
@Component
@PropertySource("classpath:tasks/user.properties")
@Slf4j
public class UserTask {
  @Value("${user.send-user-max-size}")
  private int sendUserMaxSize;
  private final UserService userService;

  @Scheduled(cron = "${user.task.schedule}")
  public void doTask() {
    log.info("上传用户数据任务开始");
    List<UserDataDTO> users = userService.getLocalUsers();
    if (null != users && !users.isEmpty()) {
      List<String> res = sendUsersToOpenCenter(users);
      log.info("上传用户数据任务结束, 共上传 {} 条数据, 返回结果为: {}", users.size(), res);
    } else {
      log.info("无新数据需要上传, 上传用户数据任务结束");
    }
  }

  private List<String> sendUsersToOpenCenter(List<UserDataDTO> users) {
    List<String> res = new ArrayList<>();
    int partNum = users.size()/sendUserMaxSize + 1;
    for (int i = 0; i < partNum; i++) {
      boolean isLast = (i == (partNum - 1));
      int start = i * sendUserMaxSize;
      int end = isLast ? (users.size() - 1) : (i + 1) * sendUserMaxSize;
      List<UserDataDTO> tempUsers = new ArrayList<>(users.subList(start, end));
      res.add(userService.sendUsersToOpenCenter(tempUsers));
    }
    return res;
  }

  @Autowired
  public UserTask(UserService userService) {
    this.userService = userService;
  }
}
