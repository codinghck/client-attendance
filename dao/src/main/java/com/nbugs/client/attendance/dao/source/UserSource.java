package com.nbugs.client.attendance.dao.source;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author hck
 * @date 2018/10/22 5:30 PM
 */
@Data
@Component
@Configuration
@PropertySource("classpath:tasks/user.properties")
@ConfigurationProperties(prefix = "user")
public class UserSource {
  private String schedule;
  private String getUserSql;
  private String orgId;
  private String sendUserUrl;
  private int sendUserMaxSize;
  private int taskTimeOut;
  private String executePositionFile;
}
