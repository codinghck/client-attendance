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
@PropertySource("classpath:tasks/attendance.properties")
@ConfigurationProperties(prefix = "attendance")
public class AttendanceSource {
  private String schedule;
  private String attendanceSql;
  private String timeFormat;
  private String terminalAttendanceUrl;
  private String sendAttendanceMaxSize;
  private String localDir;
}
