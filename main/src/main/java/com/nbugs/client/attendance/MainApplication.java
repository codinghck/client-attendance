package com.nbugs.client.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author hongtiancai
 * @date 2018/10/22 2:26 PM
 */
@SpringBootApplication
public class MainApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(MainApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    System.out.println("go===============================");
    return application.sources(MainApplication.class);
  }
}
