package com.wqy.server;

import cn.hutool.core.util.RandomUtil;
import com.wqy.server.utils.SendVerifyCodeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/8/8 23:18
 */
@SpringBootTest
public class SendMessageTest {

    @Test
    public void test01() throws Exception {
        String vCode = ((Integer)RandomUtil.randomInt(100000, 999999)).toString();
        System.out.println(vCode);
        SendVerifyCodeUtil.send("18310070520", vCode);
    }

    @Test
    public void test02(){
        for (int i = 0; i < 1000; i++) {
            String num = ((Integer)RandomUtil.randomInt(100000, 999999)).toString();
            System.out.println(num);
        }
    }
}
