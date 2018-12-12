package com.nbugs.client.attendance.task.tasks.thread;

import lombok.Data;

/**
 * @author hck 2018/12/12 11:30 AM
 */
@Data
public class TaskStatus {
  private boolean isSuccess;

  public TaskStatus() {
    isSuccess = false;
  }
}
