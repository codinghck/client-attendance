package com.nbugs.client.attendance.task.tasks;

import com.nbugs.client.attendance.dao.pojo.UserDataDTO;
import com.nbugs.client.attendance.service.UserService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class UserTask {
  @Value("${user.send-user-max-size}")
  private int sendUserMaxSize;
  private final UserService userService;

  @Scheduled(cron = "${user.task.schedule}")
  public void doTask() {
    SimpleDateFormat smt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    System.out.println("UserTask: " + smt.format(new Date()));
    List<UserDataDTO> users = userService.getLocalUsers();
    if (null != users && !users.isEmpty()) {
      sendUsersToOpenCenter(users);
    }
  }

  private void sendUsersToOpenCenter(List<UserDataDTO> users) {
    int partNum = users.size()/sendUserMaxSize + 1;
    for (int i = 0; i < partNum; i++) {
      boolean isLast = (i == (partNum - 1));
      int start = i * sendUserMaxSize;
      int end = isLast ? (users.size() - 1) : (i + 1) * sendUserMaxSize;
      List<UserDataDTO> tempUsers = new ArrayList<>(users.subList(start, end));
      userService.sendUsersToOpenCenter(tempUsers);
    }
  }

  @Autowired
  public UserTask(UserService userService) {
    this.userService = userService;
  }
}
