package com.wqy.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wqy
 * @description: 封装违章属性
 * @version: 1.0
 * @date: 2023/7/24 11:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_illegal_driving")
public class IllegalDriving {

    /**
     * 违章id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 违章原因
     */
    private String illegalDrivingName;

    /**
     * 违章地点
     */
    private String illegalDrivingLocation;

    /**
     * 违章时间
     */
    private String illegalDrivingTime;

    /**
     * 罚款
     */
    private int fine;

    /**
     * 扣分数
     */
    private int deductScore;

    /**
     * 违章是否处理
     */
    private boolean isDeal;

    /**
     * 用户id
     */
    private String userId;
}
