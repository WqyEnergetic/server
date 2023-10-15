package com.wqy.server.service;

import com.wqy.server.pojo.IllegalDriving;
import com.wqy.server.vo.R;

import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/7/24 13:09
 */
public interface IllegalDrivingService {

    /**
     * 查询违法记录接口
     *
     * @param postCode 为1代表客户要查找之前处理的，为0代表查找未处理的
     * @return
     */
    List<IllegalDriving> selectIllegalDrivingRecord(Integer postCode, String token);

    /**
     * 消除违章记录
     *
     * @param data
     * @return
     */
    R eliminateIllegalRecord(Map<String,Object> data);
}
