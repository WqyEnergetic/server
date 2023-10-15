package com.wqy.server.controller;

import com.alibaba.fastjson2.JSON;
import com.wqy.server.enums.status.MessageInfo;
import com.wqy.server.pojo.Car;
import com.wqy.server.pojo.City;
import com.wqy.server.pojo.Store;
import com.wqy.server.service.OpeningStoreService;
import com.wqy.server.utils.JsonStringParseUtil;
import com.wqy.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: wqy
 * @description: 营业网点的控制层
 * @version: 1.0
 * @date: 2023/5/19 20:27
 */
@Api(tags = "营业网点功能")
@RestController
@RequestMapping("/stores")
@Slf4j
@CrossOrigin
public class OpeningStoresController {

    @Resource
    private OpeningStoreService openingStoreService;

    /**
     * 将List<Store>类型的数据整理json格式
     *
     * @param stores
     * @return
     */
    private List<Map<String, Object>> arrangeToJSON(List<Store> stores) {
        List<Map<String, Object>> res = new ArrayList<>();
        for (int i = 0; i < stores.size(); i++) {
            Store store = stores.get(i);
            Map<String, Object> storeTmp = new HashMap<>();
            storeTmp.put("id", store.getId());
            storeTmp.put("name", store.getName());
            storeTmp.put("city", store.getCity());
            storeTmp.put("location", store.getLocation());
            storeTmp.put("hotline", store.getHotline());
            storeTmp.put("openTime", store.getOpenTime());
            storeTmp.put("closeTime", store.getCloseTime());
            storeTmp.put("workTimes", JSON.parseArray(store.getWorkTimes()));

            List<Map<String, Object>> cars = new ArrayList<>();
            for (int j = 0; j < store.getCars().size(); j++) {
                Car car = store.getCars().get(j);
                Map<String, Object> map = new HashMap<>();
                map.put("oilVolume", car.getOilVolume());
                map.put("powerType", car.getPowerType());
                map.put("id", car.getId());
                map.put("name", car.getName());
                map.put("emission", car.getEmission());
                map.put("rentPrice", car.getRentPrice());
                map.put("box", car.getBox());
                map.put("sits", car.getSits());
                map.put("transmission", car.getTransmission());
                map.put("satisfyRate", car.getSatisfyRate());
                map.put("carType", car.getCarType());
                map.put("currentStore",car.getCurrentStore());
                map.put("arrivalStore",car.getArrivalStore());
                map.put("currentCity",car.getCurrentCity());
                map.put("arrivalCity",car.getArrivalCity());
                map.put("carpooling", car.isCarpooling());
                map.put("location", store.getLocation());
                cars.add(map);
            }
            storeTmp.put("cars", cars);

            res.add(storeTmp);

        }
        return res;
    }

    /**
     * 将Store类型的数据整理json格式
     *
     * @param store
     * @return
     */
    private Map<String, Object> arrangeToJSON(Store store) {
        Map<String, Object> res = new HashMap<>();

        res.put("id", store.getId());
        res.put("name", store.getName());
        res.put("city", store.getCity());
        res.put("location", store.getLocation());
        res.put("hotline", store.getHotline());
        res.put("openTime", store.getOpenTime());
        res.put("closeTime", store.getCloseTime());
        res.put("workTimes", JSON.parseArray(store.getWorkTimes()));

        List<Map<String, Object>> cars = new ArrayList<>();
        for (int j = 0; j < store.getCars().size(); j++) {
            Car car = store.getCars().get(j);
            Map<String, Object> map = new HashMap<>();
            map.put("oilVolume", car.getOilVolume());
            map.put("powerType", car.getPowerType());
            map.put("id", car.getId());
            map.put("name", car.getName());
            map.put("emission", car.getEmission());
            map.put("currentStore",car.getCurrentStore());
            map.put("arrivalStore",car.getArrivalStore());
            map.put("rentPrice", car.getRentPrice());
            map.put("box", car.getBox());
            map.put("location", store.getLocation());
            map.put("sits", car.getSits());
            map.put("transmission", car.getTransmission());
            map.put("satisfyRate", car.getSatisfyRate());
            map.put("currentCity",car.getCurrentCity());
            map.put("arrivalCity",car.getArrivalCity());
            map.put("carType", car.getCarType());
            map.put("carpooling", car.isCarpooling());
            cars.add(map);
        }
        res.put("cars", cars);

        return res;
    }

    /**
     * 从数据库中得到所有有车门店的城市
     *
     * @return
     */
    @ApiOperation("从数据库中得到所有门店所在城市")
    @GetMapping("/getAllStoreCityInfo")
    public R cities() {
        log.info("调用了cities方法");
        List<City> cities = openingStoreService.searchAllCities();

        if (cities == null || cities.size() == 0) {
            return R.error();
        }

        //以下的数据是将服务器返回的数据变为json数据
        Map<Character, List<Map<String, Object>>> cityMap = new HashMap<>();
        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            char initial = city.getInitial();

            if (!cityMap.containsKey(initial)) {
                cityMap.put(initial, new ArrayList<>());
            }

            Map<String, Object> cityData = new HashMap<>();
            cityData.put("id", city.getId());
            cityData.put("name", city.getName());
            cityMap.get(initial).add(cityData);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (char initial = 'A'; initial <= 'Z'; initial++) {
            List<Map<String, Object>> citiesForInitial = cityMap.getOrDefault(initial, new ArrayList<>());
            if (!citiesForInitial.isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("initial", String.valueOf(initial));
                map.put("cities", citiesForInitial);
                result.add(map);
            }
        }
        log.info("离开cities方法");
        return R.ok().setData(result);
    }

    /**
     * 从数据库中得到所有的常见城市
     *
     * @return
     */
    @GetMapping("/getCommandCity")
    @ApiOperation("从数据库中得到所有的常见城市")
    public R commonCity() {
        log.info("调用了commonCity方法");
        List<City> commonCites = openingStoreService.getCommonCities();

        if (commonCites.size() == 0) {
            return R.error();
        }
        log.info("离开commonCity方法");
        return R.ok().setData(commonCites);
    }

    /**
     * 获取一个城市下的所有门店
     *
     * @param data json对象里带有城市
     * @return
     */
    @ApiOperation("获取一个城市下的所有门店")
    @PostMapping("/getAllStoresInACity")
    public R getAllStoresInACity(@RequestBody String data) {
        log.info("调用了getAllStoresInACity方法");
        //解析出城市名
        String city = JsonStringParseUtil.parseToString(data, "city");

        log.info("进入/stores/getAllStoresInACity接口中");
        log.info("解析出的城市名是：{}", city);

        //获得当前城市下所有门店的信息
        List<Store> stores = openingStoreService.getAllStores(city);

        //判断是否查询出门店信息
        if (stores == null) {
            return R.error().setMessage(MessageInfo.SELECT_FAIL.getType()).setData("stores", stores).setData("city", city);
        }

        List<Map<String, Object>> list = arrangeToJSON(stores);
        log.info("离开getAllStoresInACity方法");
        return new R().setData(
                new HashMap() {{
                    put("stores", list);
                    put("city", city);
                }});
    }


    /**
     * 通过店名获取店名名下的汽车信息
     *
     * @param data 店名
     * @return 汽车信息
     */
    @PostMapping("/getStoreInfoByStoreName")
    @ApiOperation("通过店名获取店的信息")
    public R getCarsByStoreName(@RequestBody String data) {
        //从data中提取id
        log.info("调用了getCarsByStoreName方法");
        String name = JsonStringParseUtil.parseToString(data, "name");

        Store store = openingStoreService.getStoreByName(name);

        log.info("离开getCarsByStoreName方法");
        return R.ok().setData(arrangeToJSON(store));
    }

    /**
     * 通过门店id获取店名信息
     *
     * @param data 门店id
     * @return 汽车信息
     */
    @PostMapping("/getStoreInfoByStoreId")
    @ApiOperation("通过门店id获取店名信息")
    public R getCarsByStoreId(@RequestBody String data) {
        log.info("调用了getCarsByStoreId方法");
        //从data中提取id
        String id = JsonStringParseUtil.parseToString(data, "id");

        Store store = openingStoreService.getStoreById(id);

        if (store == null) {
            return R.error().setData("store", store);
        }
        log.info("离开getCarsByStoreId方法");
        return R.ok().setData(arrangeToJSON(store));
    }
}
