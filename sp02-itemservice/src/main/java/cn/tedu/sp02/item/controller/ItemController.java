package cn.tedu.sp02.item.controller;

import cn.tedu.sp01.pojo.Item;
import cn.tedu.sp01.service.ItemService;
import cn.tedu.web.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@Slf4j
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Value("${server.port}")
    private int port;

    @GetMapping("/{orderId}")
    public JsonResult<List<Item>> getItems(@PathVariable String orderId) throws InterruptedException {
        log.info("获取订单的商品列表，orderId："+orderId);

        if (Math.random() < 0.9) {
            long t = new Random().nextInt(5000);
            log.info("延迟： "+t);
            Thread.sleep(t);
        }

        List<Item> items = itemService.getItems(orderId);
        return JsonResult.ok().data(items).msg("port="+port);
    }

    @PostMapping("/decreaseNumber")
    public JsonResult decreaseNumber(@RequestBody List<Item> items) {
        itemService.decrease(items);
        return JsonResult.ok().msg("减少商品库存成功");
    }
}
