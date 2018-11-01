package com.nbugs.client.attendance.dao;

import com.hongtiancai.base.util.common.base.BaseUtil;
import com.nbugs.client.attendance.dao.pojo.DeptDataDTO;
import com.nbugs.client.attendance.dao.source.DeptSource;
import com.nbugs.client.attendance.dao.util.PropsUtil;
import com.nbugs.client.attendance.dao.util.Util;
import java.sql.ResultSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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

  private final JdbcTemplate deptJdbcTemp;
  private final DeptSource source;
  private final static String FILE_NULL_TIP = "数据执行位置文件不存在, 请参考 {} 配置文件中的 {} 项";

  public List<DeptDataDTO> getAttendance() {
    String lastId = getLastId();
    if (BaseUtil.isStrNull(lastId)) {
      log.error(FILE_NULL_TIP, "dept.properties", "dept.execute-position-file");
      return null;
    }
    List<DeptDataDTO> res = deptJdbcTemp.query(
        source.getGetDeptSql(), new Object[]{getLastId()}, (rs, rowNum) -> getDtoFromRs(rs));
    setLastId(res);
    return res;
  }

  private String getLastId() {
    String key = "dept.last-execute-id";
    return PropsUtil.getProp(source.getExecutePositionFile(), key);
  }

  private void setLastId(List<DeptDataDTO> res) {
    if (null != res && res.size() > 0) {
      String key = "dept.last-execute-id";
      String lastId = res.get(res.size() - 1).getDataId();
      PropsUtil.setProp(source.getExecutePositionFile(), key, lastId);
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
