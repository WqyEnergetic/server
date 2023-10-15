package com.wqy.server.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wqy.server.pojo.Coupon;
import com.wqy.server.service.CouponService;
import com.wqy.server.utils.JsonStringParseUtil;
import com.wqy.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description: 优惠券功能的控制层
 * @version: 1.0
 * @date: 2023/5/19 23:09
 */
@Api(tags = "优惠券功能")
@Slf4j
@RestController
@RequestMapping("/coupon")
@CrossOrigin
public class CouponController {

    @Resource
    private CouponService couponService;

    /**
     * 从数据库中得到用户的优惠券
     *
     * @param data 前端传来的json对象
     * @return
     */
    @ApiOperation("从数据库中得到用户的优惠券")
    @PostMapping("/get")
    public R getCoupons(@RequestBody String data) {
        log.info("进入getCoupons方法");
        log.info("data={}",data);
        String status = JsonStringParseUtil.parseToString(data, "status");
        String token = JsonStringParseUtil.parseToString(data, "token");

        log.info("离开getCoupons方法");
        return couponService.getCoupons(status,token);
    }
}
