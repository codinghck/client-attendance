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
  @Value("${user.org-id}")
  private String orgId;

  public List<UserDataDTO> getUsers() {
    return userJdbcTemp.query(getUserSql, new Object[]{lastExecuteId}, (rs, rowNum) -> {
      UserDataDTO dataDTO = new UserDataDTO();
      dataDTO.setDataId(rs.getInt("id") + "");
      dataDTO.setOrgId(orgId);
      dataDTO.setUserId(Util.getByRs(rs, "user_id"));
      dataDTO.setUserName(Util.getByRs(rs, "user_name"));
      dataDTO.setCard(Util.getByRs(rs, "card"));
      dataDTO.setDeptId(Util.getByRs(rs, "dept_id"));
      dataDTO.setDeptName(Util.getByRs(rs, "dept_name"));
      return dataDTO;
    });
  }

  @Autowired
  public UserDAO(
      @Qualifier("userJdbcTemplate") JdbcTemplate userJdbcTemp) {
    this.userJdbcTemp = userJdbcTemp;
  }
}
