package com.wqy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wqy.server.dto.IndexCarSearchDTO;
import com.wqy.server.pojo.Car;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: wqy
 * @description: 提供首页功能的服务接口
 * @version: 1.0
 * @date: 2023/7/23 21:55
 */
public interface IndexPageSearchMapper extends BaseMapper<IndexCarSearchDTO> {

    /**
     * 首页查询
     *
     * @param name 店名
     * @return
     */
    List<Car> indexCarSearch(String name);
}
