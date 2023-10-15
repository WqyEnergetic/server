package com.wqy.server.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.wqy.server.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.wqy.server.constant.RedisConstants.LOGIN_USER_KEY;
import static com.wqy.server.constant.RedisConstants.LOGIN_USER_TTL;

/**
 * @author: wqy
 * @description: 登录拦截器
 * @version: 1.0
 * @date: 2023/2/8 22:24
 */
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("进入拦截器");

        //1.获取请求头中的token
        String token = request.getHeader("authorization");

        if (StrUtil.isBlank(token)) {
            //不存在token，拦截，返回401
            response.setStatus(401);
            return false;
        }

        //2.基于token获取redis中的用户
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash()
                .entries(LOGIN_USER_KEY + token);

        //3.判断用户是否存在
        if (userMap.isEmpty()) {
            //3.1 不存在，拦截，返回401
            response.setStatus(401);
            return false;
        }

        //4.将查询到的Hash数据转为UserDTO对象
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);

        //5.存在，保存用户信息到ThreadLocal
        UserHolder.saveUser(userDTO);

        //6.刷新token有效期
        stringRedisTemplate.expire(LOGIN_USER_KEY + token, LOGIN_USER_TTL, TimeUnit.MINUTES);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }

}
