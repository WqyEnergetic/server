package com.wqy.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqy.server.dto.IndexCarSearchDTO;
import com.wqy.server.mapper.CarMapper;
import com.wqy.server.mapper.IndexPageSearchMapper;
import com.wqy.server.pojo.Car;
import com.wqy.server.service.IndexPageSearchService;
import com.wqy.server.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description: 提供首页功能的服务
 * @version: 1.0
 * @date: 2023/7/23 21:54
 */
@Service
@Slf4j
@Transactional
public class IndexPageSearchServiceImpl extends ServiceImpl<IndexPageSearchMapper, IndexCarSearchDTO> implements IndexPageSearchService {

    @Resource
    private IndexPageSearchMapper indexPageSearchMapper;

    /**
     * 查找可租的车
     *
     * @param data
     * @return
     */
    @Override
    public R searchCars(Map<String, Object> data) {

        log.info("data={}",data);

        //开始最终查询
        //组装查询条件
        List<Car> list = indexPageSearchMapper.indexCarSearch((String) data.get("storeName"));

        if (list.size() == 0) {
            //数据库没有任何汽车信息
            log.info("searchCars方法没有汽车信息");
            return R.error();
        }

        //有汽车信息，正常返回即可。
        return R.ok().setData(list);
    }
}
