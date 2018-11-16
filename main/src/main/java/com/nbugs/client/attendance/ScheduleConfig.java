package com.nbugs.client.attendance;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 定时任务调用一个线程池中的线程
 * @author hck
 * @date 2018/11/15 11:35 PM
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {
  @Override
  public void configureTasks(@NotNull ScheduledTaskRegistrar taskRegistrar) {
    //参数传入一个size为10的线程池
    taskRegistrar.setScheduler(newScheduledThreadPool(10));
  }
}
