package com.wqy.server.controller;

import com.alibaba.fastjson2.JSON;
import com.wqy.server.pojo.Car;
import com.wqy.server.service.CarpoolingCarService;
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
 * @description: 顺风车控制层
 * @version: 1.0
 * @date: 2023/5/19 20:057
 */
@Api(tags = "顺风车功能")
@RestController
@RequestMapping("/carpooling")
@Slf4j
@CrossOrigin
public class CarpoolingCarController {

    @Resource
    private CarpoolingCarService carpoolingCarService;

    /**
     * 整理最终返回的data属性。
     * @param cars
     * @return
     */
    private List<Map<String,Object>> arrangeToJSON(List cars){

        List<Map<String,Object>> res = new ArrayList<>();

        if (cars == null) {
            return res;
        }

        //整理数据格式
        for (int i = 0; i < cars.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            Car car = (Car) cars.get(i);
            map.put("id",car.getId());
            map.put("currentStore",car.getCurrentStore());
            map.put("name",car.getName());
            map.put("emission",car.getEmission());
            map.put("carpooling",car.isCarpooling());
            map.put("mileage",car.getMileage());
            map.put("carAge",car.getCarAge());
            map.put("transmission",car.getTransmission());
            map.put("satisfyRate",car.getSatisfyRate());
            map.put("powerType",car.getPowerType());
            map.put("sits",car.getSits());
            map.put("carType",car.getCarType());
            map.put("rentPrice",car.getRentPrice());
            map.put("currentCity",car.getCurrentCity());
            map.put("arrivalCity",car.getArrivalCity());
            map.put("poolingLimitTime",car.getPoolingLimitTime());
            map.put("location",car.getStore().getLocation());
            map.put("arrivalStore",car.getArrivalStore());
            map.put("oilVolume",car.getOilVolume());
            res.add(map);
        }

        return res;
    }

    /**
     * 获取顺风车的过滤器过滤出的顺风车信息
     *
     * @param data
     * @return
     */
    @ApiOperation("过滤所需的顺风车信息")
    @PostMapping("/getCarpoolingCars")
    public R getCarpoolingCars(@RequestBody String data) {

        log.info("进入到getCarpoolingCars方法中");
        log.info("从前端得到的参数是:{}",data);

        Map<String,Object> parse = (Map<String,Object>) JSON.parse(data);

        List<Car> list = carpoolingCarService.getFilteredData(parse);

        return R.ok().setData(arrangeToJSON(list));
    }

    /**
     * 加载顺风车功能时初始化顺风车列表
     * @return
     */
    @GetMapping("/initList")
    public R initList() {

        log.info("已进入到initList方法中");

        List<Car> list = carpoolingCarService.initCarpoolingList();

        if (list == null) {
            log.info("已返回到controller的initList方法中");
            log.info("离开initList方法");
            return R.error();
        }
        log.info("离开initList方法");
        return R.ok().setData(arrangeToJSON(list));
    }
}
