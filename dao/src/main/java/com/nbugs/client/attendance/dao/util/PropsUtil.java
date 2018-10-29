package com.nbugs.client.attendance.dao.util;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * @author hongtiancai
 * @date 2018/10/26 3:52 PM
 */
@Slf4j
public class PropsUtil {
  public static void setProp(String filePath, String key, String value) {
    try {
      PropertiesConfiguration config = new PropertiesConfiguration(filePath);
      config.setAutoSave(true);
      setProperty(config, filePath, key, value);
    } catch (ConfigurationException e) {
      log(e);
    }
  }

  private static void setProperty(
      PropertiesConfiguration config, String filePath, String key, String value) {
    if (null == getProp(filePath, key)) {
      config.addProperty(key, value);
    } else {
      config.setProperty(key, value);
    }
  }

  public static String getProp(String filePath, String key) {
    try {
      createIfNotExist(filePath);
      PropertiesConfiguration config = new PropertiesConfiguration(filePath);
      return config.getString(key);
    } catch (ConfigurationException e) {
      log(e);
      return null;
    }
  }

  private static void createIfNotExist(String path) {
    File file = new File(path);
    if (!file.exists()) {
      createFile(file);
    }
  }

  private static void createFile(File file) {
    try {
      if (file.createNewFile()) {
        log.info("file create success, path = {}, name = {}",
            file.getPath(), file.getName());
      }
    } catch (IOException e) {
      log(e);
    }
  }

  private static void log(Exception e) {
    log.error("prop err, e = {}, msg = {}, stackTrace = {}",
        e, e.getMessage(), e.getStackTrace());
  }
}
