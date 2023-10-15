package com.wqy.server;

import com.alibaba.fastjson2.JSON;
import com.wqy.server.mapper.*;
import com.wqy.server.pojo.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wqy
 * @description: 添加数据测试类
 * @version: 1.0
 * @date: 2023/7/22 11:39
 */
@SpringBootTest
public class DataBaseTests {

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private CarMapper carMapper;

    @Resource
    private CityMapper cityMapper;

    @Resource
    private TripRecordMapper tripRecordMapper;

    @Resource
    private IllegalDrivingMapper illegalDrivingMapper;

    /**
     * 对t_car表添加测试数据
     */
    @Test
    public void testInsertCars(){
        Car car = new Car();
        car.setPowerType("92号汽油");
        car.setOilVolume(50);
        car.setCarType("小轿车");
        car.setCarpooling(false);
        car.setSale(false);
        car.setRent(true);
        car.setCurrentCity("北京");
        car.setTransmission("自动");
        car.setSits(5);
        car.setSatisfyRate(0.99);
        car.setName("日产轩逸");
        car.setRentPrice(17000);
        car.setEmission("1.5L");
        car.setBox("三厢车");
        car.setCurrentStore("北京西站取车点");
        carMapper.insert(car);
    }

    /**
     * 对t_store表添加测试数据
     */
    @Test
    public void testInsertStores(){
        Store store = new Store();
        List<String> list = new ArrayList<>();
        list.add("8:00");
        list.add("9:00");
        list.add("10:00");
        list.add("12:00");
        list.add("14:00");
        list.add("16:00");
        list.add("18:00");
        String workTimes = JSON.toJSONString(list);
        store.setName("银川站店");
        store.setHotline("010-1013 0000");
        store.setWorkTimes(workTimes);
        store.setOpenTime(list.get(0));
        store.setCloseTime(list.get(list.size() - 1));
        store.setCity("银川");
        store.setLocation("银川市金凤区上海西路惠北巷1号");
        storeMapper.insert(store);
    }

    /**
     * 添加城市数据
     */
    @Test
    public void testInsertCity(){
        City city = new City();
        city.setCommonCity(false);
        city.setName("银川");
        city.setInitial('Y');
        cityMapper.insert(city);
    }

    /**
     * 添加旅行记录
     */
    @Test
    public void testTravelRecord(){
        Trip record = new Trip();
        record.setCarId("1684406701422571522");
        record.setRentCarName("哈弗H6");
        record.setStartTripCity("北京");
        record.setStartTripStore("北京站取车点");
        record.setEndTripCity("北京");
        record.setEndTripStore("北京站取车点");
        record.setGetCarTime("2022-11-24 9:30");
        record.setReturnCarTime("2022-11-27 9:30");
        record.setRentPrice(650);
        //record.setFinished(true);
        tripRecordMapper.insert(record);
    }

    @Test
    public void testIllegalDrivingRecord(){
        IllegalDriving illegalDriving = new IllegalDriving();
        illegalDriving.setDeductScore(6);
        illegalDriving.setIllegalDrivingTime("2023-8-6 17:30");
        illegalDriving.setIllegalDrivingLocation("京昆高速主收费站-出京");
        illegalDriving.setFine(200);
        illegalDriving.setIllegalDrivingName("占用应急车道行驶");
        illegalDriving.setDeal(true);
        illegalDriving.setId("1256");
        illegalDrivingMapper.insert(illegalDriving);
    }

}
