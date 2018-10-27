package com.nbugs.client.attendance.task.tasks;

import com.nbugs.client.attendance.dao.pojo.DeptDataDTO;
import com.nbugs.client.attendance.service.DeptService;
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
 * @date 2018/10/22 4:07 PM
 * client-attendance
 */
@Component
@PropertySource("classpath:tasks/dept.properties")
@Slf4j
public class DeptTask {
  @Value("${dept.send-dept-max-size}")
  private int sendDeptMaxSize;
  private final DeptService deptService;

  @Scheduled(cron = "${dept.schedule}")
  public void doTask() {
    log.info("上传组织数据任务开始");
    List<DeptDataDTO> depts = deptService.getLocalDepts();
    if (null != depts && !depts.isEmpty()) {
      List<String> res = sendDeptsToOpenCenter(depts);
      log.info("上传组织数据任务结束, 共上传 {} 条数据, 返回结果为: {}", depts.size(), res);
    } else {
      log.info("无新数据需要上传, 上传组织数据任务结束");
    }
  }

  private List<String> sendDeptsToOpenCenter(List<DeptDataDTO> depts) {
    List<String> res = new ArrayList<>();
    int partNum = depts.size()/sendDeptMaxSize + 1;
    for (int i = 0; i < partNum; i++) {
      boolean isLast = (i == (partNum - 1));
      int start = i * sendDeptMaxSize;
      int end = isLast ? (depts.size() - 1) : (i + 1) * sendDeptMaxSize;
      List<DeptDataDTO> tempDepts = new ArrayList<>(depts.subList(start, end));
      res.add(deptService.sendDeptsToOpenCenter(tempDepts));
    }
    return res;
  }

  @Autowired
  public DeptTask(DeptService deptService) {
    this.deptService = deptService;
  }
}
