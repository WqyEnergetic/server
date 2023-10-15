package com.wqy.server.controller;

import com.wqy.server.pojo.Trip;
import com.wqy.server.service.GenerateRentOrderService;
import com.wqy.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


/**
 * @author: wqy
 * @description: 生成租车订单的控制层
 * @version: 1.0
 * @date: 2023/7/6 10:30
 */
@Api(tags = "生成租车订单")
@RestController
@RequestMapping("/generateOrder")
@CrossOrigin
public class GenerateOrderController {

    @Resource
    private GenerateRentOrderService generateRentOrderService;

    /**
     * 计算订单的价格
     *
     * @param data
     * @return
     */
    @ApiOperation("计算价格")
    @PostMapping("/getPrice")
    public R getPrice(@RequestBody Map<String, Object> data) {
        System.out.println(data);
        return generateRentOrderService.getTotalRentCarPrice(data);
    }

    /**
     * 支付订单
     *
     * @param trip
     * @return
     */
    @ApiOperation("支付订单")
    @PostMapping("/pay/{token}")
    public R pay(@RequestBody Trip trip, @PathVariable String token) {
        return generateRentOrderService.payBill(trip, token);
    }

    /**
     * 检查用户支付密码
     *
     * @param data password+token
     * @return
     */
    @ApiOperation("检查用户支付密码")
    @PostMapping("/checkPassword")
    public R checkPassword(@RequestBody Map<String, Object> data) {
        return generateRentOrderService.checkPwd(data);
    }

}
