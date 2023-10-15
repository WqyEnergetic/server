package com.wqy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wqy.server.pojo.Car;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description: 对t_car表提供通用方法
 * @version: 1.0
 * @date: 2023/7/22 21:49
 */
public interface CarMapper extends BaseMapper<Car> {

    /**
     * 查询出过滤后的结果
     *
     * @param data
     * @return
     */
    List<Car> selectFiltered(@Param("data") Map<String, Object> data);

    /**
     * 初始化顺风车列表
     *
     * @return
     */
    List<Car> init();

}
