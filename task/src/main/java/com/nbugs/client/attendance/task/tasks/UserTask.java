package com.nbugs.client.attendance.task.tasks;

import com.nbugs.client.attendance.task.source.UserSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final UserSource source;

  @Scheduled(cron = "${schedule}")
  public void doTask() {
    System.out.println("UserTask:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    System.out.println("===: " + source.getDbUrl());
  }

  @Autowired
  public UserTask(UserSource source) {
    this.source = source;
  }
}
