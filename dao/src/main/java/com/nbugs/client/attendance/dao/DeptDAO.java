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
  @Value("${dept.db.last-execute-id}")
  private String lastExecuteId;

  /**
   * 正式使用或测试的时候再把这个代码复制到TODO下面，因为没有测试数据库直接写相应的 sql
   * dataDTO.setDeptId(rs.getString("dept_id"));
   * dataDTO.setDeptName(rs.getString("dept_name"));
   * dataDTO.setParentId(rs.getString("parent_id"));
   */
  public List<DeptDataDTO> getAttendance() {
    return deptJdbcTemp.query(getDeptSql, new Object[]{lastExecuteId}, (rs, rowNum) -> {
      DeptDataDTO dataDTO = new DeptDataDTO();
      dataDTO.setDataId(rs.getInt("id") + "");
      // TODO: 正式使用或测试的时候再取消注释
      return dataDTO;
    });
  }

  @Autowired
  public DeptDAO(
      @Qualifier("deptJdbcTemplate") JdbcTemplate deptJdbcTemp) {
    this.deptJdbcTemp = deptJdbcTemp;
  }
}
