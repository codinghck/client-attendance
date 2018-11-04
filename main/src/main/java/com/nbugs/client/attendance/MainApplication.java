package com.nbugs.client.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author hck
 * @date 2018/10/22 2:26 PM
 */
@EnableScheduling
@SpringBootApplication
public class MainApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(MainApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(MainApplication.class);
  }
}
