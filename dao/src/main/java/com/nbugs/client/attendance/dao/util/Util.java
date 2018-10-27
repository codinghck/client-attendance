package com.nbugs.client.attendance.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author hongtiancai
 * create: 2018/10/26 11:17 AM
 */
public class Util {
  public static String getByRs(ResultSet rs, String name) {
    try {
      return rs.getString(name);
    } catch (SQLException e) {
      return "";
    }
  }

  public static String timeToMills(String str, String pattern) {
    if (null == str || "".equals(str)) {
      return "";
    }
    if (!isMillsError(str)) {
      return str;
    }
    if (!isFmtError(str, pattern)) {
      return timeFmt(str, pattern);
    }
    return "";
  }

  private static boolean isFmtError(String date, String pattern) {
    SimpleDateFormat format = new SimpleDateFormat(pattern);
    try {
      format.parse(date);
      return false;
    } catch (ParseException e) {
      return true;
    }
  }

  private static boolean isMillsError(Object mills) {
    try {
      java.util.Date date = new java.util.Date();
      date.setTime(Long.valueOf(mills.toString()));
      return false;
    } catch (Exception e) {
      return true;
    }
  }

  private static String timeFmt(String str, String pattern) {
    try {
      return String.valueOf(new SimpleDateFormat(pattern).parse(str).getTime());
    } catch (ParseException e) {
      return "";
    }
  }
}
