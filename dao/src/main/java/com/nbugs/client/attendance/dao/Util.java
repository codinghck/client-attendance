package com.nbugs.client.attendance.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author hongtiancai
 * create 2018/10/26 11:17 AM
 */
class Util {
  static String getByRs(ResultSet rs, String name) {
    try {
      return rs.getString(name);
    } catch (SQLException e) {
      return "";
    }
  }

  static String[] getByRsArr(ResultSet rs, String name) {
    try {
      return (String[]) rs.getArray(name).getArray();
    } catch (SQLException e) {
      return new String[]{};
    }
  }

  static String timeToMills(String str, String pattern) {
    if (null == str || "".equals(str)) {
      return "";
    }
    if (!isMillsError(str)) {
      return str;
    }
    if (!isFmtError(str, pattern)) {
      try {
        return String.valueOf(new SimpleDateFormat(pattern).parse(str).getTime());
      } catch (ParseException e) {
        return "";
      }
    }
    return "";
  }

  private static boolean isFmtError(String date, String pattern) {
    SimpleDateFormat format = new SimpleDateFormat(pattern);
    try {
      format.parse(date);
    } catch (ParseException e) {
      return true;
    }
    return false;
  }

  private static boolean isMillsError(Object mills) {
    try {
      java.util.Date date = new java.util.Date();
      date.setTime(Long.valueOf(mills.toString()));
    } catch (Exception e) {
      return true;
    }
    return false;
  }
}
