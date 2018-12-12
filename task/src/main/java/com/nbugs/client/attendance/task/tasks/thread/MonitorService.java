package com.nbugs.client.attendance.task.tasks.thread;

/**
 * @author hck 2018/12/12 11:16 AM
 */
public interface MonitorService {

  /**
   * 异步监控任务
   * @param timeout 超时时间
   * @param target 目标线程
   * @param status 线程状态
   */
  void monitor(long timeout, Thread target, TaskStatus status);
}
