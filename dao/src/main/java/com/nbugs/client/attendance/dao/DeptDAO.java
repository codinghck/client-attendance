package com.nbugs.client.attendance.dao;

import com.nbugs.client.attendance.dao.pojo.DeptDataDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author 洪天才
 * @date 2018/10/22 11:25 PM client-attendance
 */
@Repository
@PropertySource("classpath:tasks/dept.properties")
public class DeptDAO {
  private final JdbcTemplate deptJdbcTemp;

  @Value("${dept.db.get-dept-sql}")
  private String getDeptSql;
  @Value("${dept.org-id}")
  private String orgId;
  @Value("${dept.local-dir}")
  private String localDir;

  public List<DeptDataDTO> getAttendance() {
    String lastId = PropsUtil.getProp(localDir + "dept.properties", "dept.db.last-execute-id");
    List<DeptDataDTO> res = deptJdbcTemp.query(getDeptSql, new Object[]{null == lastId ? 0 : lastId}, (rs, rowNum) -> {
      DeptDataDTO dataDTO = new DeptDataDTO();
      dataDTO.setDataId(rs.getInt("id") + "");
      dataDTO.setOrgId(orgId);
      dataDTO.setDeptId(Util.getByRs(rs, "dept_id"));
      dataDTO.setDeptName(Util.getByRs(rs, "dept_name"));
      dataDTO.setParentId(Util.getByRs(rs, "parent_id"));
      return dataDTO;
    });
    if (res.size() > 0) {
      PropsUtil.setProp(localDir + "dept.properties", "dept.db.last-execute-id", res.get(res.size() - 1).getDataId());
    }
    return res;
  }

  @Autowired
  public DeptDAO(
      @Qualifier("deptJdbcTemplate") JdbcTemplate deptJdbcTemp) {
    this.deptJdbcTemp = deptJdbcTemp;
  }
}
