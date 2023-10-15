package com.wqy.server.controller;

import com.alibaba.fastjson2.JSON;
import com.wqy.server.pojo.Car;
import com.wqy.server.service.SecondHandsCarService;
import com.wqy.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description: 二手车信息控制层
 * @version: 1.0
 * @date: 2023/5/20 11:35
 */

@Api(tags = "二手车功能")
@RestController
@RequestMapping("/secondHandsCars")
@Slf4j
@CrossOrigin
public class SecondHandsCarsController {

    @Resource
    private SecondHandsCarService secondHandsCarService;

    /**
     * 整理最终返回的data属性。
     * @param cars
     * @return
     */
    private List<Map<String,Object>> arrangeToJSON(List cars){
        List<Map<String,Object>> res = new ArrayList<>();
        //整理数据格式
        for (int i = 0; i < cars.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            Car car = (Car) cars.get(i);
            map.put("id",car.getId());
            map.put("name",car.getName());
            map.put("transferTimes",car.getTransferTimes());
            map.put("secondHandsCarLocation",car.getSecondHandsCarLocation());
            map.put("emissionStandard",car.getEmissionStandard());
            map.put("mileage",car.getMileage());
            map.put("carAge",car.getCarAge());
            map.put("saleTime",car.getSaleTime());
            map.put("transmission",car.getTransmission());
            map.put("salePrice",car.getSalePrice());
            map.put("newCarTaxPrice",car.getNewCarTaxPrice());
            map.put("powerType",car.getPowerType());
            res.add(map);
        }
        return res;
    }

    /**
     * 从数据库中得到所需的二手车
     *
     * @param data 前端传来的过滤条件
     * @return
     */
    @ApiOperation("从数据库中得到所需的二手车")
    @PostMapping("/get")
    public R getSecondHandsCars(@RequestBody String data) {

        log.info("进入到getSecondHandsCars方法中");
        Map<String,Object> parseData = (Map<String,Object>) JSON.parse(data);
        log.info("前端传来的data:{}",data);

        List<Car> cars = secondHandsCarService.filterate(parseData);

        if (cars == null) {
            return R.error();
        }

        log.info("离开getSecondHandsCars方法");
        return R.ok().setData(arrangeToJSON(cars));
    }

    @ApiOperation("初始化二手车列表")
    @GetMapping("/init")
    public R initSecondHandsCarList(){

        log.info("进入到initSecondHandsCarList方法中");

        List cars = secondHandsCarService.init();
        System.out.println(cars);
        if (cars == null) {
            log.info("未查询到数据");
            log.info("离开initSecondHandsCarList方法");
            return R.error();
        }

        log.info("离开initSecondHandsCarList方法");
        return R.ok().setData(arrangeToJSON(cars));
    }

}
