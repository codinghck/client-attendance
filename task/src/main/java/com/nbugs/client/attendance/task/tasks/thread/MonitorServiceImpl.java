package com.nbugs.client.attendance.task.tasks.thread;

import com.github.hckisagoodboy.base.util.common.base.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author hck 2018/12/12 11:17 AM
 */
@Slf4j
@Service
public class MonitorServiceImpl implements MonitorService {

  @Override
  @Async("monitorServiceExecutor")
  public void monitor(long timeout, Thread target, TaskStatus status) {
    log.info("开始监控线程 {} 的执行", target.getName());
    doMonitor(timeout, target, status);
    log.info("结束监控线程 {} 的执行", target.getName());
  }

  private void doMonitor(long timeout, Thread target, TaskStatus status) {
    try {
      timeOutMonitor(timeout, target, status);
    } catch (ThreadTimeoutException e) {
      log.warn("线程 {} 执行超时, 执行时间应在 {} 毫秒以内, 已终止该线程", target.getName(), timeout);
    } catch (InterruptedException e) {
      log.error("监控执行线程 {} 被打断", Thread.currentThread());
    } catch (Throwable e) {
      LogUtils.logThrowable(log, e, "监控执行线程 " + Thread.currentThread() + " 发生预期外异常");
    }
  }

  private void timeOutMonitor(long timeout, Thread target, TaskStatus status)
      throws InterruptedException {
    Thread.sleep(timeout);
    if(!status.isSuccess()) {
      target.interrupt();
      throw new ThreadTimeoutException();
    }
  }
}
