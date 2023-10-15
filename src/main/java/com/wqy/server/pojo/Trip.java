package com.wqy.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wqy
 * @description: 封装行程记录的信息
 * @version: 1.0
 * @date: 2023/7/23 22:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_trip_record")
public class Trip {

    /**
     * 行程id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 被租用的车的id
     */
    private String carId;

    /**
     * 租赁的车名
     */
    private String rentCarName;

    /**
     * 取车城市
     */
    private String startTripCity;

    /**
     * 取车门店
     */
    private String startTripStore;

    /**
     * 还车城市
     */
    private String endTripCity;

    /**
     * 还车门店
     */
    private String endTripStore;

    /***
     * 取车时间
     */
    private String getCarTime;

    /**
     * 还车时间
     */
    private String returnCarTime;

    /**
     * 共消费金额（元）
     */
    private Integer rentPrice;

    /**
     * 租赁状态
     */
    private String status;

    /**
     * 用户id
     */
    private String userId;
}
