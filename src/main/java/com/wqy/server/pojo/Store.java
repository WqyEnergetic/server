package com.wqy.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mysql.cj.xdevapi.JsonString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description: 门店信息
 * @version: 1.0
 * @date: 2023/7/22 14:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_store")
public class Store {

    /**
     * 营业网点的id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 营业网点的名字
     */
    private String name;

    /**
     * 营业网点的地址
     */
    private String location;

    /**
     *
     */
    private String city;

    /**
     * 营业网点的联系方式
     */
    private String hotline;

    /**
     * 门店开业时间
     */
    private String openTime;

    /**
     * 门店打烊时间
     */
    private String closeTime;

    /**
     * 所有可供出租的时间
     */
    private String workTimes;

    /**
     * 门店下的所有可供出租的汽车
     */
    private List<Car> cars;
}
