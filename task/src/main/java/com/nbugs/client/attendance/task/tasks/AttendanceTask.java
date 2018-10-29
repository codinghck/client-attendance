package com.nbugs.client.attendance.task.tasks;

import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import com.nbugs.client.attendance.dao.source.AttendanceSource;
import com.nbugs.client.attendance.service.AttendanceService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 洪天才
 * @date 2018/10/22 4:07 PM
 */
@Component
@Slf4j
@PropertySource("classpath:tasks/attendance.properties")
public class AttendanceTask {
  private final AttendanceService attendanceService;
  private final AttendanceSource source;

  @Scheduled(cron = "${attendance.schedule}")
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
    int max = Integer.valueOf(source.getSendAttendanceMaxSize());
    boolean needExPart = attendances.size() % max != 0;
    int partNum = attendances.size() / max + (needExPart ? 1 : 0);
    for (int i = 0; i < partNum; i++) {
      boolean isLast = (i == (partNum - 1));
      int start = i * max;
      int end = isLast ? (attendances.size() - 1) : (i + 1) * max;
      List<AttendanceDataDTO> tempAttendances = new ArrayList<>(attendances.subList(start, end));
      res.add(attendanceService.sendAttendanceMsg(tempAttendances));
    }
    return res;
  }

  @Autowired
  public AttendanceTask(
      AttendanceService attendanceService,
      AttendanceSource attendanceSource) {
    this.attendanceService = attendanceService;
    this.source = attendanceSource;
  }
}
