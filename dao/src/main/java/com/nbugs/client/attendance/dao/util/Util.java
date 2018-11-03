package com.nbugs.client.attendance.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author hck
 * @date 2018/10/26 11:17 AM
 */
public class Util {
  /**
   * <p>从 <code>ResultSet</code> 对象中根据 {@code name} 获取查询结果</p>
   *
   * @param rs <code>ResultSet</code> 对象
   * @param name 需要获取查询结果的 {@code name}
   * @return ResultSet 对象中该 {@code name} 的查询结果, 没有则返回空字符串
   */
  public static String getByRs(ResultSet rs, String name) {
    try {
      return rs.getString(name);
    } catch (SQLException e) {
      return "";
    }
  }
}
