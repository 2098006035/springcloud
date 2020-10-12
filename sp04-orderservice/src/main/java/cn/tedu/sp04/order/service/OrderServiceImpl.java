package cn.tedu.sp04.order.service;

import cn.tedu.sp01.pojo.Item;
import cn.tedu.sp01.pojo.Order;
import cn.tedu.sp01.pojo.User;
import cn.tedu.sp01.service.OrderService;
import cn.tedu.sp04.order.feign.ItemFeignClient;
import cn.tedu.sp04.order.feign.UserFeignClient;
import cn.tedu.web.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ItemFeignClient itemFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public Order getOrder(String orderId) {
        // 调用商品服务，获取订单的商品列表
        JsonResult<List<Item>> items = itemFeignClient.getItems(orderId);
        // 调用用户服务，获取用户
        JsonResult<User> user = userFeignClient.getUser(7);

        log.info("获取订单，orderId = "+orderId);

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user.getData());
        order.setItems(items.getData());
        return order;
    }

    @Override
    public void addOrder(Order order) {
        // 新订单中购买的商品，减少商品库存
        itemFeignClient.decreaseNumber(order.getItems());
        // 增加用户积分
        userFeignClient.addScore(order.getUser().getId(), 1000);

        log.info("保存订单："+order);
    }
}
