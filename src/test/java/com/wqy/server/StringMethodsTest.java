package com.wqy.server;

import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/7/27 10:40
 */
@SpringBootTest
public class StringMethodsTest {

    @Test
    void a(){
        String saleRange = "15,20,30-40";
        if (saleRange.contains(",")) {
            System.out.println("有 - 划的范围");
            String[] range = saleRange.split(",");
            for (int i = 0; i < range.length; i++) {
                System.out.println(range[i]);
            }
        }
    }

    @Test
    public void b(){
        System.out.println(IdUtil.objectId().length());
    }
}
