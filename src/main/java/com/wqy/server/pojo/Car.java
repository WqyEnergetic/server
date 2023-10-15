package com.wqy.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wqy
 * @description: 汽车信息
 * @version: 1.0
 * @date: 2023/5/23 10:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_car")
public class Car {

    /**
     * 汽车的id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 汽车的油箱容积
     */
    private Integer oilVolume;

    /**
     * 汽车的名字
     */
    private String name;

    /**
     * 汽车的排量
     */
    private String emission;

    /**
     * 汽车的租价
     */
    private Integer rentPrice;

    /**
     * 是否是顺风车
     */
    private boolean isCarpooling;

    /**
     * 顺风车限租时间(如:2天)
     */
    private Integer poolingLimitTime;

    /**
     * 是否是二手车
     */
    private boolean isSale;

    /**
     * 是否是出租的车
     */
    private boolean isRent;

    /**
     * 最终目的城市
     */
    private String arrivalCity;

    /**
     * 最终目的门店
     */
    private String arrivalStore;

    /**
     * 查找当前车辆所在的城市
     */
    private String currentCity;

    /**
     * 几箱车
     */
    private String box;

    /**
     * 车辆所在的门店
     */
    private String currentStore;

    /**
     * 座位数
     */
    private Integer sits;

    /**
     * 变速箱类型
     */
    private String transmission;

    /**
     * 客户满意度
     */
    private Double satisfyRate;

    /**
     * 汽车类型（SUV...）
     */
    private String carType;

    /**
     * 过户次数
     */
    private Integer transferTimes;

    /**
     * 总行驶里程（单位：万公里）
     */
    private Integer mileage;

    /**
     * 车龄（单位：年）
     */
    private Integer carAge;

    /**
     * 上市日期
     */
    private String saleTime;

    /**
     * 排放标准(国V)
     */
    private String emissionStandard;

    /**
     * 售卖价格（万元）
     */
    private Double salePrice;

    /**
     * 新车含税价
     */
    private Integer newCarTaxPrice;

    /**
     * 动力类型(92/95汽油，纯电...)
     */
    private String powerType;

    /**
     * 汽车门店
     */
    private Store store;

    /**
     * 二手车的具体位置
     */
    private String secondHandsCarLocation;

}