package com.wqy.server;

import com.wqy.server.mapper.OrderInfoMapper;
import com.wqy.server.pojo.OrderInfo;
import com.wqy.server.service.impl.GenerateRentOrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/8/20 21:35
 */
@SpringBootTest
public class OtherTest {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private GenerateRentOrderServiceImpl generateRentOrderService;

    @Test
    public void testGetCurrentTime(){
        //获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(System.currentTimeMillis());
        OrderInfo orderInfo = new OrderInfo();
        System.out.println(format);
    }

    @Test
    public void testGetTotalDays(){
//        int totalDays = generateRentOrderService.getTotalDays("2023-08-21 06:00 -- 2023-08-23 06:00");  //2
//        int totalDays2 = generateRentOrderService.getTotalDays("2023-08-21 14:00~2023-08-23 14:01");    //3
//        int totalDays3 = generateRentOrderService.getTotalDays("2023-08-21 14:00 ~ 2023-08-23 14:00");  //2
//        int totalDays4 = generateRentOrderService.getTotalDays("2023-08-21 14:00 -- 2023-08-23 13:59"); //2
//        int totalDays5 = generateRentOrderService.getTotalDays("2023-8-26 14:00 ~ 2023-8-31 11:00");    //5
//        int totalDays6 = generateRentOrderService.getTotalDays("2023-08-24 6:00~2023-08-25 6:00");      //1
//        int totalDays7 = generateRentOrderService.getTotalDays("2023-12-24 6:00~2023-12-25 6:00");      //1
//        int totalDays8 = generateRentOrderService.getTotalDays("2023-12-4 6:00~2023-12-4 6:01");      //1
//        int totalDays9 = generateRentOrderService.getTotalDays("2023-12-04 6:00~2023-12-05 7:00");      //2
//        System.out.println(totalDays);
//        System.out.println(totalDays2);
//        System.out.println(totalDays3);
//        System.out.println(totalDays4);
//        System.out.println(totalDays5);
//        System.out.println(totalDays6);
//        System.out.println(totalDays7);
//        System.out.println(totalDays8);
//        System.out.println(totalDays9);
    }
}
