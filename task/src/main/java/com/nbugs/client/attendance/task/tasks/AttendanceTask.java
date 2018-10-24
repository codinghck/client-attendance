package com.nbugs.client.attendance.task.tasks;

import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import com.nbugs.client.attendance.service.AttendanceService;
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
 * @date 2018/10/22 4:07 PM client-attendance
 */
@Component
@PropertySource("classpath:tasks/attendance.properties")
@Slf4j
public class AttendanceTask {
  @Value("${attendance.send-attendance-max-size}")
  private int attendanceDataMaxSize;
  private final AttendanceService attendanceService;

  @Scheduled(cron = "${attendance.task.schedule}")
  public void doTask() {
    log.info("上传考勤任务开始");
    List<AttendanceDataDTO> attendances = attendanceService.getLocalAttendances();
    if (null != attendances && !attendances.isEmpty()) {
      List<String> res = sendAttendanceToOpenCenter(attendances);
      log.info("上传考勤任务结束, 共上传 {} 条数据, 返回结果为: {}", attendances.size(), res);
    } else {
      log.info("无新数据需要上传, 上传考勤任务结束");
    }
  }

  private List<String> sendAttendanceToOpenCenter(List<AttendanceDataDTO> attendances) {
    List<String> res = new ArrayList<>();
    int partNum = attendances.size() / attendanceDataMaxSize + 1;
    for (int i = 0; i < partNum; i++) {
      boolean isLast = (i == (partNum - 1));
      int start = i * attendanceDataMaxSize;
      int end = isLast ? (attendances.size() - 1) : (i + 1) * attendanceDataMaxSize;
      List<AttendanceDataDTO> tempAttendances = new ArrayList<>(attendances.subList(start, end));
      res.add(attendanceService.sendAttendanceMsg(tempAttendances));
    }
    return res;
  }

  @Autowired
  public AttendanceTask(AttendanceService attendanceService) {
    this.attendanceService = attendanceService;
  }
}
