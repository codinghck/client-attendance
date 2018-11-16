package com.nbugs.client.attendance.task.tasks;

import com.github.hckisagoodboy.base.util.common.base.ListUtils;
import com.nbugs.client.attendance.dao.pojo.DeptDataDTO;
import com.nbugs.client.attendance.dao.source.DeptSource;
import com.nbugs.client.attendance.service.DeptService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author hck
 * @date 2018/10/22 4:07 PM
 */
@Component
@PropertySource("classpath:tasks/dept.properties")
@Slf4j
public class DeptTask {
  private final DeptService deptService;
  private final DeptSource source;

  @Scheduled(cron = "${dept.schedule}")
  public void doTask() {
    log.info("上传组织数据任务开始");
    List<DeptDataDTO> depts = deptService.getLocalDepts();
    if (!ListUtils.isEmpty(depts)) {
      List<String> res = sendDeptsToOpenCenter(depts);
      log.info("上传组织数据任务结束, 共上传 {} 条数据, 返回结果为: {}", depts.size(), res);
    } else {
      log.info("无新数据需要上传, 上传组织数据任务结束");
    }
  }

  private List<String> sendDeptsToOpenCenter(List<DeptDataDTO> depts) {
    List<String> res = new ArrayList<>();
    int max = source.getSendDeptMaxSize();
    int partNum = depts.size() / max + 1;
    for (int i = 0; i < partNum; i++) {
      boolean isLast = (i == (partNum - 1));
      int start = i * max;
      int end = isLast ? (depts.size() - 1) : (i + 1) * max + 1;
      List<DeptDataDTO> tempDepts = new ArrayList<>(depts.subList(start, end));
      res.add(deptService.sendDeptsToOpenCenter(tempDepts));
    }
    return res;
  }

  @Autowired
  public DeptTask(DeptService deptService, DeptSource deptSource) {
    this.deptService = deptService;
    this.source = deptSource;
  }
}
