package com.wqy.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wqy
 * @description: 封装用户登录信息的数据传输对象(即dto)
 * @version: 1.0
 * @date: 2023/8/8 22:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    /**
     * 用户手机号码
     */
    private String id;

    /**
     * 用户手机号码
     */
    private String phoneNumber;

    /**
     * 用户的验证码
     */
    private String verifyCode;

    /**
     * 用户余额
     */
    private Integer accountMoney;
}
