package com.nbugs.client.attendance.dao.source;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 洪天才
 * @date 2018/10/23 1:45 PM
 */
@Data
@Component
@Configuration
@PropertySource("classpath:tasks/open_center.properties")
@ConfigurationProperties(prefix = "open.center")
public class OpenCenterSource {
  private String tokenUrl;
  private String clientId;
  private String clientSecret;
  private String grantType;
  private String responseType;
  private String leaveUrl;
}
