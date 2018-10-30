package com.nbugs.client.attendance.web.controller;

import com.hongtiancai.base.util.validation.pojo.NormalDataOut;
import com.hongtiancai.base.util.validation.request.RqResult;
import com.hongtiancai.base.util.validation.request.RqResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hongtiancai
 * @date 2018/10/30 3:56 PM
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
  @RequestMapping(value = "/go", method = RequestMethod.GET)
  public RqResult<NormalDataOut> isOnLeaveTime() {
    System.out.println("let's go my friend!");
    NormalDataOut<String> outData = new NormalDataOut<>("", "success");
    return new RqResultHandler<NormalDataOut>().getSuccessInvalidRqRes(outData);
  }
}
