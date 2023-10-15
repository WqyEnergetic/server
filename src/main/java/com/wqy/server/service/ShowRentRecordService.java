package com.wqy.server.service;

import com.wqy.server.pojo.Trip;

import java.util.List;

/**
 * @author: wqy
 * @description: 显示所有租车信息的服务接口
 * @version: 1.0
 * @date: 2023/7/27 11:36
 */
public interface ShowRentRecordService {

    /**
     * 查找完成订单
     *
     * @return
     */
    List<Trip> showFinished(String token);

    /**
     * 查找未出行订单
     *
     * @return
     */
    List<Trip> showWillDo(String token);

    /**
     * 查找正在进行中订单
     *
     * @return
     */
    List<Trip> showRenting(String token);
}
