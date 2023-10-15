package com.wqy.server.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wqy
 * @description: 用户数据的实体类
 * @version: 1.0
 * @date: 2023/8/5 22:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * 用户手机号码
     */
    private String phoneNumber;

    /**
     * 用户余额
     */
    private Integer accountMoney;

    /**
     * 账号是否删除
     */
    @TableLogic
    private boolean isDelete;

    /**
     * 身份证正面
     */
    private String frontIdCardImagePath;

    /**
     * 身份证反面
     */
    private String backIdCardImagePath;

    /**
     * 驾驶证正面
     */
    @TableField("front_dl_image_path")
    private String frontDLImagePath;

    /**
     * 驾驶证反面
     */
    @TableField("back_dl_image_path")
    private String backDLImagePath;

}
