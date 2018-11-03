package com.nbugs.client.attendance.web.controller;

import com.hongtiancai.base.util.common.request.NormalOut;
import com.hongtiancai.base.util.common.request.ReqRes;
import com.hongtiancai.base.util.common.request.ResHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hck
 * @date 2018/10/30 3:56 PM
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
  @RequestMapping(value = "/go", method = RequestMethod.GET)
  public ReqRes<NormalOut> isOnLeaveTime() {
    System.out.println("let's go my friend!");
    NormalOut<String> outData = new NormalOut<>("", "success");
    return new ResHandler<NormalOut>().getSuccessRqRes(outData);
  }
}
