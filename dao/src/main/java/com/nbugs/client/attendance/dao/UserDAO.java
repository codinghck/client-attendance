package com.nbugs.client.attendance.dao;

import com.github.hckisagoodboy.base.util.common.base.ListUtils;
import com.github.hckisagoodboy.base.util.common.base.PropertiesUtils;
import com.github.hckisagoodboy.base.util.common.base.StrUtils;
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
  private final JdbcTemplate jdbcTemplate;
  private final UserSource source;

  public List<UserDataDTO> getUsers() {
    List<UserDataDTO> res = jdbcTemplate.query(
        source.getGetUserSql(), new Object[]{getLastId()}, (rs, rowNum) -> getDtoFromRs(rs));
    setLastId(res);
    return res;
  }

  @SneakyThrows({ConfigurationException.class})
  private String getLastId() {
    String lastId = PropertiesUtils.getFirstValue(source.getExecutePositionFile());
    log.info("上次用户执行位置为 {}", lastId);
    return lastId;
  }

  @SneakyThrows({ConfigurationException.class})
  private void setLastId(List<UserDataDTO> res) {
    if (!ListUtils.isEmpty(res)) {
      String lastId = res.get(res.size() - 1).getDataId();
      log.info("下次用户执行开始位置为 {}", lastId);
      PropertiesUtils.setFirstValue(source.getExecutePositionFile(), lastId);
    }
  }

  private UserDataDTO getDtoFromRs(ResultSet rs) {
    UserDataDTO dataDTO = new UserDataDTO();
    dataDTO.setDataId(Util.getByRs(rs, "id"));
    dataDTO.setOrgId(source.getOrgId());
    dataDTO.setUserName(Util.getByRs(rs, "user_name"));
    dataDTO.setDeptId(Util.getByRs(rs, "dept_id"));
    dataDTO.setDeptName(Util.getByRs(rs, "dept_name"));
    String userId = Util.getByRs(rs, "user_id");
    String cardId = Util.getByRs(rs, "card");
    if (userId != null) {
      userId = StrUtils.addLeftIfLenNotEnough(userId, '0', 10);
    }
    if (cardId != null) {
      cardId = StrUtils.addLeftIfLenNotEnough(cardId, '0', 10);
    }
    dataDTO.setUserId(userId);
    dataDTO.setCard(cardId);
    return dataDTO;
  }

  @Autowired
  public UserDAO(
      @Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate, UserSource userSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.source = userSource;
  }
}
