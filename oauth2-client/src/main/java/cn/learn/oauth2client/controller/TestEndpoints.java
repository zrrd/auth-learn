package cn.learn.oauth2client.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shaoyijiong
 * @date 2019/6/4
 */
@RestController
public class TestEndpoints {

  /**
   * 产品查询 不需要保护
   */
  @GetMapping("/product/{id}")
  public String getProduct(@PathVariable String id) {
    //获取当前登陆信息
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return "product id : " + id;
  }

  /**
   * 订单查询 需要保护
   */
  @GetMapping("/order/{id}")
  public String getOrder(@PathVariable String id) {
    //获取当前登陆信息
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return "order id : " + id;
  }

}
