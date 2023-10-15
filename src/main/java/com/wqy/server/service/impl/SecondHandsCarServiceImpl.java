package com.wqy.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqy.server.mapper.CarMapper;
import com.wqy.server.pojo.Car;
import com.wqy.server.service.SecondHandsCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/7/25 9:48
 */
@Service
@Slf4j
@Transactional
public class SecondHandsCarServiceImpl extends ServiceImpl<CarMapper, Car> implements SecondHandsCarService {

    @Resource
    private CarMapper secondHandsCarMapper;


    @Override
    public List<Car> init() {

        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id,name,transfer_times,emission_standard,mileage,car_age," +
                "sale_time,transmission,sale_price,new_car_tax_price,power_type," +
                "second_hands_car_location" )
                    .eq("is_sale", true);

        log.info("开始从数据库中查询数据");
        List<Car> list = list(queryWrapper);
        log.info("查询完毕，数据为：{}", list);

        if (list.size() == 0) {
            log.info("未查询到数据，list.size() == 0");
        }
        log.info("离开init方法");
        return list;
    }

    @Override
    public List<Car> filterate(Map<String, Object> data) {
        log.info("进入到filterate方法中");

        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id,name,transfer_times,emission_standard,mileage,car_age," +
                "sale_time,transmission,sale_price,new_car_tax_price,power_type," +
                "second_hands_car_location" )
                    .eq("is_sale", true);

        //用户指定所在地城市
        if (!data.get("currentCity").equals("")) {
            queryWrapper.eq("current_city", data.get("currentCity"));
        }
        //用户指定汽车动力类型
        if (!data.get("powerType").equals("-")) {
            if (data.get("powerType").equals("燃油")) {
                //如果是燃油车的话就找powerType是汽油或柴油的
                queryWrapper.in("power_type", "92号汽油", "95号汽油", "柴油");
            } else {
                queryWrapper.eq("power_type", data.get("powerType"));
            }
        }
        //用户指定车龄
        if (!data.get("carAge").equals("-")) {
            String carAge = (String) data.get("carAge");
            if(carAge.contains("-")){
                int age = Integer.parseInt(carAge.split("-")[0]);
                queryWrapper.ge("car_age", age);
            }
            queryWrapper.le("car_age", data.get("carAge"));
        }
        //用户指定过户次数
        if (!data.get("transferTimes").equals("-")) {
            if (data.get("transferTimes").equals("多次")) {
                //transfer_times是多次，就改成大于
                queryWrapper.gt("transfer_times", 2);
            } else {
                queryWrapper.eq("transfer_times", data.get("transferTimes"));
            }
        }
        //用户指定变速箱类型
        if (!data.get("transmission").equals("-")) {
            queryWrapper.eq("transmission", data.get("transmission"));
        }
        //用户指定价格区间
        if (!data.get("saleRange").equals("-")) {
            String saleRange = (String) data.get("saleRange");

            if (saleRange.contains("-")) {
                String[] range = saleRange.split("-");
                //分割字符串得到的最低价格
                Double lowPrice = Double.parseDouble(range[0]);
                if (range.length == 1) {
                    //用户选择的价格在(50)万以上
                    queryWrapper.ge("sale_price",lowPrice);
                } else {
                    //分割字符串得到的最高价格
                    Double highPrice = Double.parseDouble(range[1]);
                    queryWrapper.between("sale_price",lowPrice,highPrice);
                }

            }
        }


        log.info("开始从数据库中查询数据");
        List<Car> list = list(queryWrapper);
        log.info("查询完毕，数据为：{}", list);

        if (list.size() == 0) {
            log.info("未查询到数据，list.size() == 0");
        }

        log.info("离开filterate方法");
        return list;
    }

}
