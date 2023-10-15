package com.wqy.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/7/31 9:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_coupon")
public class Coupon {

    /**
     * 优惠券id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券可用起始时间
     */
    private String startTime;

    /**
     * 优惠券可用最后时间
     */
    private String endTime;

    /**
     * 优惠券说明（字符串数组）
     */
    private String instruction;

    /**
     * 用户id
     */
    private String userId;
}
