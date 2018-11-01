package com.nbugs.client.attendance.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author hck
 * @date 2018/10/26 11:17 AM
 */
public class Util {
  public static String getByRs(ResultSet rs, String name) {
    try {
      return rs.getString(name);
    } catch (SQLException e) {
      return "";
    }
  }

  public static String timeToSecond(String str, String pattern) {
    if (null == str || "".equals(str)) {
      return "";
    }
    if (!isSecondError(str)) {
      return str;
    }
    if (!isFmtError(str, pattern)) {
      return timeFmtToSecond(str, pattern);
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

  private static boolean isSecondError(String mills) {
    try {
      java.util.Date date = new java.util.Date();
      date.setTime(Long.valueOf(mills));
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

  private static String timeFmtToSecond(String str, String pattern) {
    try {
      long second = new SimpleDateFormat(pattern).parse(str).getTime() / 1000;
      return String.valueOf(second);
    } catch (ParseException e) {
      return "";
    }
  }
}
