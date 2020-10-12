package cn.tedu.sp06.controller;

import cn.tedu.sp01.pojo.Item;
import cn.tedu.sp01.pojo.Order;
import cn.tedu.sp01.pojo.User;
import cn.tedu.web.util.JsonResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@Slf4j
public class RibbonController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/item-service/{orderId}")
    @HystrixCommand(fallbackMethod = "getItemsFB")
    public JsonResult<List<Item>> getItems(@PathVariable String orderId) {
        log.info("调用后台商品服务，获取商品列表");

        /*
        httpclient     底层
        resttemplate   高度封装
         */

        return restTemplate.getForObject(
                "http://item-service/{1}",
                JsonResult.class,
                orderId);
    }

    @PostMapping("/item-service/decreaseNumber")
    @HystrixCommand(fallbackMethod = "decreaseNumberFB")
    public JsonResult decreaseNumber(@RequestBody List<Item> items) {
        log.info("调用后台商品服务，减少商品库存");

        return restTemplate.postForObject(
                "http://item-service/decreaseNumber",
                items,
                JsonResult.class);
    }

    @GetMapping("/user-service/{userId}")
    @HystrixCommand(fallbackMethod = "getUserFB")
    public JsonResult<User> getUser(@PathVariable Integer userId) {
        return restTemplate.getForObject(
                "http://user-service/{1}",
                JsonResult.class,
                userId);
    }

    @GetMapping("/user-service/{userId}/score")
    @HystrixCommand(fallbackMethod = "addScoreFB")
    public JsonResult addScore(@PathVariable Integer userId, Integer score) {
        return restTemplate.getForObject(
                "http://user-service/{1}/score?score={2}",
                JsonResult.class,
                userId, score);
    }

    @GetMapping("/order-service/{orderId}")
    @HystrixCommand(fallbackMethod = "getOrderFB")
    public JsonResult<Order> getOrder(@PathVariable String orderId) {
        return restTemplate.getForObject(
                "http://order-service/{1}",
                JsonResult.class,
                orderId);
    }

    @GetMapping("/order-service/")
    @HystrixCommand(fallbackMethod = "addOrderFB")
    public JsonResult addOrder() {
        return restTemplate.getForObject(
                "http://order-service/",
                JsonResult.class);
    }



    ////////////////////////////////////////////////////
    public JsonResult<List<Item>> getItemsFB(String orderId) {
        return JsonResult.err().msg("获取订单的商品列表失败");
    }
    public JsonResult decreaseNumberFB(List<Item> items) {
        return JsonResult.err().msg("减少商品库存失败");
    }
    public JsonResult<User> getUserFB(Integer userId) {
        return JsonResult.err().msg("获取用户失败");
    }
    public JsonResult addScoreFB(Integer userId, Integer score) {
        return JsonResult.err().msg("增加用户积分失败");
    }
    public JsonResult<Order> getOrderFB(String orderId) {
        return JsonResult.err().msg("获取订单失败");
    }
    public JsonResult addOrderFB() {
        return JsonResult.err().msg("保存订单失败");
    }



}
