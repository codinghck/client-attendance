package com.nbugs.client.attendance.dao;

import com.hongtiancai.base.util.common.exception.UnExpectedException;
import com.hongtiancai.base.util.common.utils.ListUtil;
import com.hongtiancai.base.util.common.utils.PropertiesUtil;
import com.nbugs.client.attendance.dao.pojo.UserDataDTO;
import com.nbugs.client.attendance.dao.source.UserSource;
import com.nbugs.client.attendance.dao.util.Util;
import java.sql.ResultSet;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author hck
 * @date 2018/10/22 11:24 PM
 */
@Repository
@Slf4j
public class UserDAO {
  private final JdbcTemplate userJdbcTemp;
  private final UserSource source;

  public List<UserDataDTO> getUsers() {
    List<UserDataDTO> res = userJdbcTemp.query(
        source.getGetUserSql(), new Object[]{getLastId()}, (rs, rowNum) -> getDtoFromRs(rs));
    setLastId(res);
    return res;
  }

  @SneakyThrows({ConfigurationException.class})
  private String getLastId() {
    return PropertiesUtil.getFirstValue(source.getExecutePositionFile());
  }

  @SneakyThrows({ConfigurationException.class, UnExpectedException.class})
  private void setLastId(List<UserDataDTO> res) {
    if (!ListUtil.isEmpty(res)) {
      String lastId = res.get(res.size() - 1).getDataId();
      PropertiesUtil.setFirstValue(source.getExecutePositionFile(), lastId);
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
      @Qualifier("userJdbcTemplate") JdbcTemplate userJdbcTemp, UserSource userSource) {
    this.userJdbcTemp = userJdbcTemp;
    this.source = userSource;
  }
}
