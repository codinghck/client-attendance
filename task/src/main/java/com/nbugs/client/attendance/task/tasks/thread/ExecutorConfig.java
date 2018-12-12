package com.nbugs.client.attendance.task.tasks.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author hck 2018/12/12 11:24 AM
 */
@Slf4j
@EnableAsync
@Configuration
public class ExecutorConfig {

  @Bean
  public Executor monitorServiceExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // 配置核心线程数
    executor.setCorePoolSize(10);
    // 配置最大线程数
    executor.setMaxPoolSize(10);
    // 配置队列大小
    executor.setQueueCapacity(99999);
    // 配置线程池中的线程的名称前缀
    executor.setThreadNamePrefix("monitor-service-");
    // rejection-policy：当pool已经达到max size的时候，如何处理新任务
    // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.setThreadFactory(new BasicThreadFactory
        .Builder().namingPattern("monitor-pool-%d").daemon(true).build());
    // 执行初始化
    executor.initialize();
    return executor;
  }
}