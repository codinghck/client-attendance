package com.nbugs.client.attendance.task.tasks;

import com.github.hckisagoodboy.base.util.common.base.ListUtils;
import com.github.hckisagoodboy.base.util.common.base.LogUtils;
import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import com.nbugs.client.attendance.dao.source.AttendanceSource;
import com.nbugs.client.attendance.service.AttendanceService;
import com.nbugs.client.attendance.task.tasks.thread.MonitorService;
import com.nbugs.client.attendance.task.tasks.thread.TaskStatus;
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
@Slf4j
@PropertySource("classpath:tasks/attendance.properties")
public class AttendanceTask {

  private final AttendanceService attendanceService;
  private final AttendanceSource source;
  private final MonitorService monitor;

  @Scheduled(cron = "${attendance.schedule}")
  public void doTask() {
    try {
      TaskStatus status = new TaskStatus();
      monitor.monitor(source.getTaskTimeOut(), Thread.currentThread(), status);
      doAttendanceTask();
      status.setSuccess(true);
    } catch (Throwable e) {
      LogUtils.logThrowable(log, e, "上传考勤任务发生错误!");
    }
  }

  private void doAttendanceTask() {
    log.info("上传考勤任务开始");
    List<AttendanceDataDTO> attendances = attendanceService.getLocalAttendances();
    log.info("需要上传 {} 条数据", attendances.size());
    if (!ListUtils.isEmpty(attendances)) {
      List<String> res = sendAttendanceToOpenCenter(attendances);
      log.info("上传考勤任务结束, 共上传 {} 条数据, 返回结果为: {}", attendances.size(), res);
    } else {
      log.info("无新数据需要上传, 上传考勤任务结束");
    }
  }

  private List<String> sendAttendanceToOpenCenter(List<AttendanceDataDTO> attendances) {
    List<String> res = new ArrayList<>();
    int max = source.getSendAttendanceMaxSize();
    boolean needExPart = attendances.size() % max != 0;
    int partNum = attendances.size() / max + (needExPart ? 1 : 0);
    log.info("需上传考勤数据 {} 条, 最大上传数据 {} 条, 需要分成 {} 部分上传", attendances.size(), max, partNum);
    for (int i = 0; i < partNum; i++) {
      boolean isLast = (i == (partNum - 1));
      int start = i * max;
      int end = (isLast ? (attendances.size() - 1) : (i + 1) * max) + 1;
      List<AttendanceDataDTO> tempAttendances = new ArrayList<>(attendances.subList(start, end));
      log.info("上传考勤第 {} 部分开始, 开始索引 {}, 结束索引 {}, 需上传数据第一条示例 = {}",
          i + 1, start, end, tempAttendances.get(0));
      res.add(attendanceService.sendAttendanceMsg(tempAttendances));
    }
    return res;
  }

  @Autowired
  public AttendanceTask(
      AttendanceService attendanceService,
      AttendanceSource attendanceSource,
      MonitorService monitor) {
    this.attendanceService = attendanceService;
    this.source = attendanceSource;
    this.monitor = monitor;
  }
}
