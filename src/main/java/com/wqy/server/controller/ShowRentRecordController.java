package com.wqy.server.controller;

import com.wqy.server.pojo.Trip;
import com.wqy.server.service.ShowRentRecordService;
import com.wqy.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description: 订单查询控制层
 * @version: 1.0
 * @date: 2023/5/10 21:46
 */
@Api(tags = "订单查询功能")
@Slf4j
@RestController
@RequestMapping("/usedToRent")
@CrossOrigin
public class ShowRentRecordController {

    @Resource
    private ShowRentRecordService showRentRecordService;

    /**
     * 显示已完成订单
     *
     * @return
     */
    @ApiOperation("显示已完成订单")
    @PostMapping("/finished")
    public R finished(@RequestBody Map<String,Object> map) {

        log.info("进入finished()方法");

        String token = (String)map.get("token");
        List<Trip> trips = showRentRecordService.showFinished(token);

        if (trips == null) {
            log.info("未查询到数据");
            log.info("离开finished()方法");
            return R.error();
        }

        log.info("离开finished()方法");
        return R.ok().setData(trips);
    }

    /**
     * 显示未出行订单
     *
     * @return
     */
    @ApiOperation("显示未出行订单")
    @PostMapping("/appointment")
    public R appointment(@RequestBody Map<String,Object> map) {
        log.info("进入appointment()方法");
        String token = (String)map.get("token");
        List<Trip> trips = showRentRecordService.showWillDo(token);

        if (trips == null) {
            log.info("未查询到数据");
            log.info("离开appointment()方法");
            return R.error();
        }

        log.info("离开appointment()方法");
        return R.ok().setData(trips);
    }

    /**
     * 显示正在进行中订单
     *
     * @return
     */
    @ApiOperation("显示正在进行中订单")
    @PostMapping("/renting")
    public R renting(@RequestBody Map<String,Object> map) {
        log.info("进入renting()方法");
        String token = (String)map.get("token");
        List<Trip> trips = showRentRecordService.showRenting(token);

        if (trips == null) {
            log.info("未查询到数据");
            log.info("离开renting()方法");
            return R.error();
        }

        log.info("离开renting()方法");
        return R.ok().setData(trips);
    }

}

