package com.nbugs.client.attendance.source;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author hck
 * @date 2018/10/23 9:53 AM
 */
@Configuration
@PropertySources({ @PropertySource("classpath:tasks/attendance.properties") })
public class DataSourceConfig {
  @Primary
  @Bean(name = "dataSource")
  @Qualifier("dataSource")
  @ConfigurationProperties(prefix = "db")
  public DataSource attendanceDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "jdbcTemplate")
  public JdbcTemplate attendanceJdbcTemplate(
      @Qualifier("dataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}
