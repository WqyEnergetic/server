package com.wqy.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wqy
 * @description: 封装城市信息
 * @version: 1.0
 * @date: 2023/7/23 11:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_city")
public class City {

    /**
     * 城市邮政编码（id）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 城市名
     */
    private String name;

    /**
     * 城市的首字母
     */
    private char initial;

    /**
     * 是否是常用城市
     */
    private boolean isCommonCity;
}
