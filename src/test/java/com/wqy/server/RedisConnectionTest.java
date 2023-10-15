package com.wqy.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/8/11 9:26
 */
@SpringBootTest
public class RedisConnectionTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test01(){
        stringRedisTemplate.opsForValue().set("a","1234567890");
    }
}
