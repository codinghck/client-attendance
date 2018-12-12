package com.nbugs.client.attendance;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
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
    BasicThreadFactory factory = new BasicThreadFactory
        .Builder()
        .namingPattern("schedule-pool-%d")
        .daemon(true)
        .build();
    ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(10, factory);
    // 参数传入一个 size 为 10 的线程池
    taskRegistrar.setScheduler(executor);
  }
}
