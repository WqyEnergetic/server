package com.wqy.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqy.server.mapper.CarMapper;
import com.wqy.server.mapper.CityMapper;
import com.wqy.server.mapper.StoreMapper;
import com.wqy.server.pojo.Car;
import com.wqy.server.pojo.City;
import com.wqy.server.pojo.Store;
import com.wqy.server.service.OpeningStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: wqy
 * @description: 营业网点功能的所有服务
 * @version: 1.0
 * @date: 2023/7/22 15:34
 */
@Service
@Slf4j
@Transactional
public class OpeningStoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements OpeningStoreService {

    @Resource
    private StoreMapper openingStoreMapper;

    //注入t_car的通用方法的接口
    @Resource
    private CarMapper carMapper;

    @Resource
    private CityMapper cityMapper;

    /**
     * 除了汽车信息外其他查询条件的组装
     *
     * @param field 数据库表的字段
     * @param var   你传递的变量值
     * @return 查询条件
     */
    private QueryWrapper<Store> getSelectCondition(String field, String var) {
        //先查询出那个门店除了汽车信息外相关的数据
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        //通过id查找相关信息
        queryWrapper.select("id",
                        "name",
                        "location",
                        "hotline",
                        "city",
                        "open_time",
                        "close_time",
                        "work_times"
                ).eq(field, var);
        return queryWrapper;
    }

    /**
     * 将汽车信息加进门店信息中
     *
     * @param store
     * @return
     */
    private Store getCarsByStoreName(Store store) {

        //通过店名得到所有有关的汽车信息
        QueryWrapper<Car> carQueryWrapper = new QueryWrapper<>();
        carQueryWrapper.select("id",
                        "name",
                        "oil_volume",
                        "power_type",
                        "emission",
                        "is_carpooling",
                        "rent_price",
                        "box",
                        "sits",
                        "current_city",
                        "arrival_city",
                        "transmission",
                        "satisfy_rate",
                        "car_type",
                        "current_store",
                        "arrival_store"
                )
                .eq("current_store", store.getName()).eq("is_rent", true);
        List<Car> cars = carMapper.selectList(carQueryWrapper);

        //将汽车信息放入门店信息中
        store.setCars(cars);
        return store;
    }

    /**
     * 通过门店id查找门店信息
     *
     * @param id
     * @return
     */
    @Override
    public Store getStoreById(String id) {

        //查询出门店汽车信息外的其他信息
        Store store = openingStoreMapper.selectOne(getSelectCondition("id", id));

        //如果store是空，返回null
        if (store == null) {
            log.info("getStoreById方法查询出的门店信息是空。");
            return null;
        }

        log.info("查询到的store数据：{}", store);

        //将汽车信息加进门店信息中
        store = getCarsByStoreName(store);

        return store;
    }

    /**
     * 通过门店名查找门店信息
     *
     * @param name
     * @return
     */
    @Override
    public Store getStoreByName(String name) {
        //查询出门店汽车信息外的其他信息
        Store store = openingStoreMapper.selectOne(getSelectCondition("name", name));

        //如果store是空，返回null
        if (store == null) {
            log.info("getStoreByName方法查询出的门店信息是空。");
            return null;
        }

        //通过门店的店名
        log.info("查询到的store数据：{}", store);

        //将汽车信息加进门店信息中
        store = getCarsByStoreName(store);

        return store;
    }

    /**
     * 通过城市名获取其下所有门店
     *
     * @param city
     * @return
     */
    @Override
    public List<Store> getAllStores(String city) {

        //查询出门店汽车信息外那个城市的所有门店信息
        List<Store> list = list(getSelectCondition("city", city));

        if (list.size() == 0) {
            log.info("getAllStores方法查询出的所有的门店信息均为空。");
            return null;
        }

        //遍历list中的store
        for (int i = 0; i < list.size(); i++) {
            //依次取出每个list中的store
            Store store = list.get(i);

            //将汽车信息加进门店信息中
            store = getCarsByStoreName(store);

            //更新list的store数据
            list.set(i, store);
        }
        return list;
    }

    @Override
    public List<City> searchAllCities() {
        List<City> cities = cityMapper.selectList(null);
        log.info("所有城市列表：{}", cities);
        return cities;
    }

    @Override
    public List<City> getCommonCities() {
        QueryWrapper<City> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_common_city", true);

        return cityMapper.selectList(queryWrapper);
    }
}
