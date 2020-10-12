package cn.tedu.sp04.order.feign;

import cn.tedu.sp01.pojo.User;
import cn.tedu.web.util.JsonResult;
import org.springframework.stereotype.Component;

@Component
public class UserFeignClientFB implements UserFeignClient{
    @Override
    public JsonResult<User> getUser(Integer userId) {
        if (Math.random()<0.5) {
            User u = new User(userId, "用户名" + userId, "密码" + userId);
            return JsonResult.ok().data(u);
        }

        return JsonResult.err().msg("获取用户失败");
    }

    @Override
    public JsonResult addScore(Integer userId, Integer score) {
        return JsonResult.err().msg("增加用户积分失败");
    }
}
