package com.nbugs.client.attendance.task.tasks;

import com.github.hckisagoodboy.base.util.common.base.ListUtils;
import com.github.hckisagoodboy.base.util.common.base.LogUtils;
import com.nbugs.client.attendance.dao.pojo.UserDataDTO;
import com.nbugs.client.attendance.dao.source.UserSource;
import com.nbugs.client.attendance.service.UserService;
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
 * @date 2018/10/22 4:06 PM
 */
@Component
@PropertySource("classpath:tasks/user.properties")
@Slf4j
public class UserTask {

  private final UserService userService;
  private final UserSource source;
  private final MonitorService monitor;

  @Scheduled(cron = "${user.schedule}")
  public void doTask() {
    try {
      TaskStatus status = new TaskStatus();
      monitor.monitor(source.getTaskTimeOut(), Thread.currentThread(), status);
      doUserTask();
      status.setSuccess(true);
    } catch (Throwable e) {
      LogUtils.logThrowable(log, e, "上传用户任务发生错误!");
    }
  }

  private void doUserTask() {
    log.info("上传用户数据任务开始");
    List<UserDataDTO> users = userService.getLocalUsers();
    if (!ListUtils.isEmpty(users)) {
      List<String> res = sendUsersToOpenCenter(users);
      log.info("上传用户数据任务结束, 共上传 {} 条数据, 返回结果为: {}", users.size(), res);
    } else {
      log.info("无新数据需要上传, 上传用户数据任务结束");
    }
  }

  private List<String> sendUsersToOpenCenter(List<UserDataDTO> users) {
    List<String> res = new ArrayList<>();
    int max = source.getSendUserMaxSize();
    int partNum = users.size() / max + 1;
    log.info("需上传用户数据 {} 条, 最大上传数据 {} 条, 需要分成 {} 部分上传", users.size(), max, partNum);
    for (int i = 0; i < partNum; i++) {
      boolean isLast = (i == (partNum - 1));
      int start = i * max;
      int end = (isLast ? (users.size() - 1) : (i + 1) * max) + 1;
      List<UserDataDTO> tempUsers = new ArrayList<>(users.subList(start, end));
      log.info("上传用户第 {} 部分开始, 开始索引 {}, 结束索引 {}, 需上传数据第一条示例 = {}",
          i + 1, start, end, tempUsers.get(0));
      res.add(userService.sendUsersToOpenCenter(tempUsers));
    }
    return res;
  }

  @Autowired
  public UserTask(UserService userService, UserSource userSource, MonitorService monitor) {
    this.userService = userService;
    this.source = userSource;
    this.monitor = monitor;
  }
}
