package com.nbugs.client.attendance.task.source;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 洪天才
 * @date 2018/10/22 10:33 PM client-attendance
 */
@Data
@Component
@Configuration
@PropertySource("classpath:tasks/attendance.properties")
@ConfigurationProperties(prefix = "attendance")
public class AttendanceSource {

}
