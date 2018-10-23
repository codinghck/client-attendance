package com.nbugs.client.attendance.task.tasks;

import com.nbugs.client.attendance.dao.pojo.AttendanceDataDTO;
import com.nbugs.client.attendance.service.AttendanceService;
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
 * @date 2018/10/22 4:07 PM client-attendance
 */
@Component
@PropertySource("classpath:tasks/attendance.properties")
public class AttendanceTask {
  @Value("${attendance.send-attendance-max-size}")
  private int attendanceDataMaxSize;
  private final AttendanceService attendanceService;

  @Scheduled(cron = "${attendance.task.schedule}")
  public void doTask() {
    SimpleDateFormat smt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    System.out.println("AttendanceTask: " + smt.format(new Date()));
    List<AttendanceDataDTO> attendances = attendanceService.getLocalAttendances();
    if (null != attendances && !attendances.isEmpty()) {
      sendAttendanceToOpenCenter(attendances);
    }
  }

  private void sendAttendanceToOpenCenter(List<AttendanceDataDTO> attendances) {
    int partNum = attendances.size()/attendanceDataMaxSize + 1;
    for (int i = 0; i < partNum; i++) {
      boolean isLast = (i == (partNum - 1));
      int start = i * attendanceDataMaxSize;
      int end = isLast ? (attendances.size() - 1) : (i + 1) * attendanceDataMaxSize;
      List<AttendanceDataDTO> tempAttendances = new ArrayList<>(attendances.subList(start, end));
      attendanceService.sendAttendanceMsg(tempAttendances);
    }
  }

  @Autowired
  public AttendanceTask(AttendanceService attendanceService) {
    this.attendanceService = attendanceService;
  }
}
