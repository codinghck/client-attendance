package com.nbugs.client.attendance.dao;

import com.nbugs.client.attendance.dao.pojo.DeptDataDTO;
import com.nbugs.client.attendance.dao.source.DeptSource;
import com.nbugs.client.attendance.dao.util.PropsUtil;
import com.nbugs.client.attendance.dao.util.Util;
import java.sql.ResultSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author 洪天才
 * create: 2018/10/22 11:25 PM
 */
@Repository
public class DeptDAO {
  private final JdbcTemplate deptJdbcTemp;
  private final DeptSource source;

  public List<DeptDataDTO> getAttendance() {
    List<DeptDataDTO> res = deptJdbcTemp.query(
        source.getGetDeptSql(), new Object[]{getLastId()}, (rs, rowNum) -> getDtoFromRs(rs));
    setLastId(res);
    return res;
  }

  private String getLastId() {
    String path = source.getLocalDir() + "dept.properties";
    String key = "dept.last-execute-id";
    String lastId = PropsUtil.getProp(path, key);
    return null == lastId ? "0" : lastId;
  }

  private void setLastId(List<DeptDataDTO> res) {
    if (null != res && res.size() > 0) {
      String path = source.getLocalDir() + "dept.properties";
      String key = "dept.last-execute-id";
      String lastId = res.get(res.size() - 1).getDataId();
      PropsUtil.setProp(path, key, lastId);
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
      @Qualifier("deptJdbcTemplate") JdbcTemplate deptJdbcTemp,
      DeptSource deptSource) {
    this.deptJdbcTemp = deptJdbcTemp;
    this.source = deptSource;
  }
}
