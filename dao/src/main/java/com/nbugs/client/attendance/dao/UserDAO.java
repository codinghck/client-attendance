package com.nbugs.client.attendance.dao;

import com.nbugs.client.attendance.dao.pojo.UserDataDTO;
import com.nbugs.client.attendance.dao.source.UserSource;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author 洪天才
 * @date 2018/10/22 11:24 PM client-attendance
 */
@Repository
public class UserDAO {
  private final JdbcTemplate userJdbcTemp;
  private final UserSource source;

  public List<UserDataDTO> getUsers() {
    String lastId = PropsUtil.getProp(source.getLocalDir() + "user.properties", "user.db.last-execute-id");
    List<UserDataDTO> res = userJdbcTemp.query(source.getGetUserSql(), new Object[]{null == lastId ? 0 : lastId}, (rs, rowNum) -> {
      UserDataDTO dataDTO = new UserDataDTO();
      dataDTO.setDataId(rs.getInt("id") + "");
      dataDTO.setOrgId(source.getOrgId());
      dataDTO.setUserId(Util.getByRs(rs, "user_id"));
      dataDTO.setUserName(Util.getByRs(rs, "user_name"));
      dataDTO.setCard(Util.getByRs(rs, "card"));
      dataDTO.setDeptId(Util.getByRs(rs, "dept_id"));
      dataDTO.setDeptName(Util.getByRs(rs, "dept_name"));
      return dataDTO;
    });
    if (res.size() > 0) {
      PropsUtil.setProp(source.getLocalDir() + "user.properties", "user.db.last-execute-id", res.get(res.size() - 1).getDataId());
    }
    return res;
  }

  @Autowired
  public UserDAO(
      @Qualifier("userJdbcTemplate") JdbcTemplate userJdbcTemp,
      UserSource userSource) {
    this.userJdbcTemp = userJdbcTemp;
    this.source = userSource;
  }
}
