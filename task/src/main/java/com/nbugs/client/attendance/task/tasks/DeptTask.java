package com.nbugs.client.attendance.task.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 洪天才
 * @date 2018/10/22 4:07 PM client-attendance
 */
@Component
@PropertySource("classpath:tasks/dept.properties")
public class DeptTask {
  @Scheduled(cron = "${dept.task.schedule}")
  public void doTask() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    System.out.println("DeptTask:" + sdf.format(new Date()));
  }
}
