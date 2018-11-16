package com.nbugs.client.attendance.dao;

import com.github.hckisagoodboy.base.util.common.base.ListUtils;
import com.github.hckisagoodboy.base.util.common.base.PropertiesUtils;
import com.nbugs.client.attendance.dao.pojo.DeptDataDTO;
import com.nbugs.client.attendance.dao.source.DeptSource;
import com.nbugs.client.attendance.dao.util.Util;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author hck
 * @date 2018/10/22 11:25 PM
 */
@Repository
@Slf4j
public class DeptDAO {

  private final JdbcTemplate jdbcTemplate;
  private final DeptSource source;

  public List<DeptDataDTO> getAttendance() {
    List<DeptDataDTO> res = jdbcTemplate.query(
        source.getGetDeptSql(), new Object[]{getLastId()}, (rs, rowNum) -> getDtoFromRs(rs));
    setLastId(res);
    return res;
  }

  @SneakyThrows({ConfigurationException.class})
  private String getLastId() {
    String lastId = PropertiesUtils.getFirstValue(source.getExecutePositionFile());
    log.info("上次组织执行位置为 {}", lastId);
    return lastId;
  }

  @SneakyThrows({ConfigurationException.class})
  private void setLastId(List<DeptDataDTO> res) {
    if (!ListUtils.isEmpty(res)) {
      String lastId = res.get(res.size() - 1).getDataId();
      log.info("下次组织执行开始位置为 {}", lastId);
      PropertiesUtils.setFirstValue(source.getExecutePositionFile(), lastId);
    }
  }

  private DeptDataDTO getDtoFromRs(ResultSet rs) {
    DeptDataDTO dataDTO = new DeptDataDTO();
    dataDTO.setDataId(Util.getByRs(rs, "id"));
    dataDTO.setOrgId(source.getOrgId());
    dataDTO.setDeptId(Util.getByRs(rs, "dept_id"));
    dataDTO.setDeptName(Util.getByRs(rs, "dept_name"));
    dataDTO.setParentId(Util.getByRs(rs, "parent_id"));
    return dataDTO;
  }

  @Autowired
  public DeptDAO(
      @Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate, DeptSource deptSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.source = deptSource;
  }
}
