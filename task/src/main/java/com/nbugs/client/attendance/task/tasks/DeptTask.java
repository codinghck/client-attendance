package com.nbugs.client.attendance.task.tasks;

import com.nbugs.client.attendance.dao.pojo.DeptDataDTO;
import com.nbugs.client.attendance.service.DeptService;
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
 * @date 2018/10/22 4:07 PM
 * client-attendance
 */
@Component
@PropertySource("classpath:tasks/dept.properties")
public class DeptTask {
  @Value("${dept.send-dept-max-size}")
  private int sendDeptMaxSize;
  private final DeptService deptService;

  @Scheduled(cron = "${dept.task.schedule}")
  public void doTask() {
    SimpleDateFormat smt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    System.out.println("DeptTask: " + smt.format(new Date()));
    List<DeptDataDTO> attendances = deptService.getLocalDepts();
    if (null != attendances && !attendances.isEmpty()) {
      sendDeptsToOpenCenter(attendances);
    }
  }

  private void sendDeptsToOpenCenter(List<DeptDataDTO> depts) {
    int partNum = depts.size()/sendDeptMaxSize + 1;
    for (int i = 0; i < partNum; i++) {
      boolean isLast = (i == (partNum - 1));
      int start = i * sendDeptMaxSize;
      int end = isLast ? (depts.size() - 1) : (i + 1) * sendDeptMaxSize;
      List<DeptDataDTO> tempDepts = new ArrayList<>(depts.subList(start, end));
      deptService.sendDeptsToOpenCenter(tempDepts);
    }
  }

  @Autowired
  public DeptTask(DeptService deptService) {
    this.deptService = deptService;
  }
}
