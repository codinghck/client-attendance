package com.nbugs.client.attendance.web.controller;

import com.hck.util.request.RqResult;
import com.hck.util.request.RqResultHandler;
import com.hck.util.validate.NotEmpty;
import com.hck.util.validate.Size;
import com.hck.util.validate.pojo.NormalDataOut;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 洪天才
 * @date 2018/10/23 6:56 PM
 */
@RestController
@RequestMapping("/props")
public class PropertiesController {

  @RequestMapping(value = "/attendance/update", method = RequestMethod.GET)
  public RqResult<NormalDataOut> updateAttendance(
      @NotEmpty @Size(max = 20) @RequestParam("requestId") String requestId,
      @NotEmpty @Size(max = 4) @RequestParam("sendAttendanceMaxSize") String sendAttendanceMaxSize) {
    RqResultHandler<NormalDataOut> handler = new RqResultHandler<>();

    System.out.println("requestId: " + requestId);
    System.out.println("sendAttendanceMaxSize: " + sendAttendanceMaxSize);

    changeProps(sendAttendanceMaxSize);

    return handler.getSuccessInvalidRqRes(new NormalDataOut<>(requestId, "success"));
  }

  private void changeProps(String sendAttendanceMaxSize) {

    String profilepath = PropertiesController.class.getResource("/").getPath() + "tasks/attendance.properties";
    System.out.println("profilepath: " + profilepath);
    try {
      PropertiesConfiguration config = new PropertiesConfiguration(profilepath);
      System.out.println("attendance.send-attendance-max-size: " + config.getString("attendance.send-attendance-max-size"));

      config.setAutoSave(true);
      config.setProperty("attendance.send-attendance-max-size", sendAttendanceMaxSize);
      System.out.println("attendance.send-attendance-max-size: " + config.getString("attendance.send-attendance-max-size"));
    } catch (ConfigurationException cex) {
      System.err.println("loading of the configuration file failed");
    }
  }
}
