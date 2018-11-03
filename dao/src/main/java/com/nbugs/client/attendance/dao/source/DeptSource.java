package com.nbugs.client.attendance.dao.source;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author hck
 * @date 2018/10/22 10:33 PM
 */
@Data
@Component
@Configuration
@PropertySource("classpath:tasks/dept.properties")
@ConfigurationProperties(prefix = "dept")
public class DeptSource {
  private String schedule;
  private String getDeptSql;
  private String orgId;
  private String sendDeptUrl;
  private int sendDeptMaxSize;
  private String executePositionFile;
}
