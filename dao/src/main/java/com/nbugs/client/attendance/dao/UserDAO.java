package com.nbugs.client.attendance.dao;

import com.nbugs.client.attendance.dao.pojo.UserDataDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author 洪天才
 * @date 2018/10/22 11:24 PM client-attendance
 */
@Repository
@PropertySource("classpath:tasks/user.properties")
public class UserDAO {
  private final JdbcTemplate userJdbcTemp;

  @Value("${user.db.get-user-sql}")
  private String getUserSql;
  @Value("${user.db.last-execute-id}")
  private String lastExecuteId;

  /**
   * 正式使用或测试的时候再把这个代码复制到TODO下面，因为没有测试数据库直接写相应的 sql
   * dataDTO.setUserId(rs.getString("user_id"));
   * dataDTO.setUserName(rs.getString("user_name"));
   * dataDTO.setCard(rs.getString("card"));
   * dataDTO.setDeptId(rs.getString("dept_id"));
   * dataDTO.setDeptName(rs.getString("dept_name"));
   */
  public List<UserDataDTO> getUsers() {
    return userJdbcTemp.query(getUserSql, new Object[]{lastExecuteId}, (rs, rowNum) -> {
      UserDataDTO dataDTO = new UserDataDTO();
      dataDTO.setDataId(rs.getInt("id") + "");
      // TODO: 正式使用或测试的时候再取消注释
      return dataDTO;
    });
  }

  @Autowired
  public UserDAO(
      @Qualifier("userJdbcTemplate") JdbcTemplate userJdbcTemp) {
    this.userJdbcTemp = userJdbcTemp;
  }
}
