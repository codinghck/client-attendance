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
 * @author 洪天才
 * @date 2018/10/23 9:53 AM client-attendance
 */
@Configuration
@PropertySources({
    @PropertySource("classpath:tasks/user.properties"),
    @PropertySource("classpath:tasks/dept.properties"),
    @PropertySource("classpath:tasks/attendance.properties")
})
public class DataSourceConfig {

  @Bean(name = "attendanceDataSource")
  @Qualifier("attendanceDataSource")
  @Primary
  @ConfigurationProperties(prefix = "attendance.db")
  public DataSource attendanceDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "userDataSource")
  @Qualifier("userDataSource")
  @ConfigurationProperties(prefix = "user.db")
  public DataSource userDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "deptDataSource")
  @Qualifier("deptDataSource")
  @ConfigurationProperties(prefix = "dept.db")
  public DataSource deptDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "attendanceJdbcTemplate")
  public JdbcTemplate attendanceJdbcTemplate(
      @Qualifier("attendanceDataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean(name = "userJdbcTemplate")
  public JdbcTemplate userJdbcTemplate(
      @Qualifier("userDataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean(name = "deptJdbcTemplate")
  public JdbcTemplate deptJdbcTemplate(
      @Qualifier("deptDataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}
