package com.nbugs.client.attendance.service.source;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 洪天才
 * @date 2018/10/22 5:30 PM client-attendance
 */
@Data
@Component
@Configuration
@PropertySource("classpath:tasks/user.properties")
@ConfigurationProperties(prefix = "user")
public class UserSource {
  private String dbUrl;
}
