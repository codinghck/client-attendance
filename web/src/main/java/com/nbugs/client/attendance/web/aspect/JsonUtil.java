package com.nbugs.client.attendance.web.aspect;

import com.alibaba.fastjson.JSONObject;
import com.hck.util.validate.exception.ValidateException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hongtiancai
 * @date 2018/10/22 上午10:30
 */
public class JsonUtil {
  public static <T> List<T> jsonArrToList(String jsonArrayStr, Class<T> clazz) throws ValidateException {
    try {
      return JSONObject.parseArray(jsonArrayStr, clazz);
    } catch (Exception e) {
      throw new ValidateException("请检查 json 数组/字段参数格式");
    }
  }

  public static <T> List<T> jsonArrToList(String jsonStr, String strName, Class<T> clazz) throws Exception {
    List<T> res = new ArrayList<>();
    List list = getJsonArrayByStr(jsonStr, strName);
    for (Object obj : list) {
      res.add(jsonStrToObj(obj.toString(), clazz));
    }
    return res;
  }

  public static <T> T jsonStrToObj(String jsonStr, Class<T> clazz) throws ValidateException {
    try {
      return JSONObject.parseObject(jsonStr, clazz);
    } catch (Exception e) {
      throw new ValidateException("请检查 json 对象格式");
    }
  }

  private static List getJsonArrayByStr(String str, String strName) throws ValidateException {
    try {
      return JSONObject.parseArray(str);
    } catch (Exception e) {
      throw new ValidateException("参数 " + strName + " 不符合 json 数组格式");
    }
  }
}
