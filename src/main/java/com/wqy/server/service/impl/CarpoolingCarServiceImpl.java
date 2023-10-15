package com.wqy.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqy.server.mapper.CarMapper;
import com.wqy.server.pojo.Car;
import com.wqy.server.service.CarpoolingCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description: 提供顺风车服务的实现类
 * @version: 1.0
 * @date: 2023/7/24 13:23
 */
@Service
@Slf4j
@Transactional
public class CarpoolingCarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarpoolingCarService {

    @Resource
    private CarMapper carpoolingCarMapper;

    /**
     * 初始化顺风车列表
     *
     * @return
     */
    @Override
    public List<Car> initCarpoolingList() {

        log.info("进入到initCarpoolingList方法中");

        //查询
        log.info("准备查询~");

        //查询
        List<Car> list = carpoolingCarMapper.init();

        //判断是否查询出结果
        if (list.size() == 0) {
            log.info("没有顺风车");
            return null;
        }
        log.info("离开initCarpoolingList方法");
        return list;
    }

    /**
     * 得到过滤后的顺风车列表
     *
     * @param data
     * @return
     */
    @Override
    public List<Car> getFilteredData(Map<String, Object> data) {

        log.info("已进入到getFilteredData中");
        log.info("getFilteredData传入数据:{}", data);

        //查询
        List<Car> list = carpoolingCarMapper.selectFiltered(data);

        //是否存在查询结果
        if (list.size() == 0) {
            log.info("查询到的顺风车结果为空");
        }

        return list;
    }

}
