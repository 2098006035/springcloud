package cn.tedu.sp03.user.service;

import cn.tedu.sp01.pojo.User;
import cn.tedu.sp01.service.UserService;
import cn.tedu.web.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Value("${sp.user-service.users}")
    private String userJson;

    @Override
    public User getUser(Integer userId) {
        log.info("userJson = "+userJson);
        List<User> users = JsonUtil.from(userJson, new TypeReference<List<User>>() {});
        for (User u:users) {
            if (userId.equals(u.getId())) {
                return u;
            }
        }
        return new User(userId, "用户名"+userId, "密码"+userId);
    }

    @Override
    public void addScore(Integer userId, Integer score) {
        log.info("userId="+userId+", score="+score);
    }
}
