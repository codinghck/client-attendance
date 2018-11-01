package com.nbugs.client.attendance.dao;

import com.nbugs.client.attendance.dao.pojo.UserDataDTO;
import com.nbugs.client.attendance.dao.source.UserSource;
import com.nbugs.client.attendance.dao.util.PropsUtil;
import com.nbugs.client.attendance.dao.util.Util;
import java.sql.ResultSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author hck
 * @date 2018/10/22 11:24 PM
 */
@Repository
public class UserDAO {
  private final JdbcTemplate userJdbcTemp;
  private final UserSource source;

  public List<UserDataDTO> getUsers() {
    List<UserDataDTO> res = userJdbcTemp.query(
        source.getGetUserSql(), new Object[]{getLastId()}, (rs, rowNum) -> getDtoFromRs(rs));
    setLastId(res);
    return res;
  }

  private String getLastId() {
    String path = source.getLocalDir() + "user.properties";
    String key = "user.last-execute-id";
    String lastId = PropsUtil.getProp(path, key);
    return null == lastId ? "0" : lastId;
  }

  private void setLastId(List<UserDataDTO> res) {
    if (null != res && res.size() > 0) {
      String path = source.getLocalDir() + "user.properties";
      String key = "user.last-execute-id";
      String lastId = res.get(res.size() - 1).getDataId();
      PropsUtil.setProp(path, key, lastId);
    }
  }

  private UserDataDTO getDtoFromRs(ResultSet rs) {
    UserDataDTO dataDTO = new UserDataDTO();
    dataDTO.setDataId(Util.getByRs(rs, "id"));
    dataDTO.setOrgId(source.getOrgId());
    dataDTO.setUserId(Util.getByRs(rs, "user_id"));
    dataDTO.setUserName(Util.getByRs(rs, "user_name"));
    dataDTO.setCard(Util.getByRs(rs, "card"));
    dataDTO.setDeptId(Util.getByRs(rs, "dept_id"));
    dataDTO.setDeptName(Util.getByRs(rs, "dept_name"));
    return dataDTO;
  }

  @Autowired
  public UserDAO(
      @Qualifier("userJdbcTemplate") JdbcTemplate userJdbcTemp,
      UserSource userSource) {
    this.userJdbcTemp = userJdbcTemp;
    this.source = userSource;
  }
}
