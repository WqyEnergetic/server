package com.wqy.server.enums.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: wqy
 * @description: 后台给前台的响应代码
 * @version: 1.0
 * @date: 2023/7/22 9:54
 */
@AllArgsConstructor
@Getter
public enum StatusCode {

    /**
     * 用户支付成功
     */
    SUCCESS_PAY(101),

    /**
     * 用户未支付
     */
    NOT_PAY(102),

    /**
     * 手机号修改成功
     */
    PHONE_NUM_SUCCESS_MODIFY(103),

    /**
     * 响应成功
     */
    SUCCESS(200),

    /**
     * 用户余额不足
     */
    NOT_ENOUGH_MONEY(201),


    /**
     * 响应失败
     */
    ERROR(400),

    /**
     * 登录状态失效
     */
    OUT_OF_LOGIN(401),

    /**
     * 验证码错误
     */
    VERIFY_CODE_ERROR(402),

    /**
     * 密码错误
     */
    PASSWORD_ERROR(403),

    /**
     * 手机号冲突
     */
    DUPLICATE_PHONE_NUM(404);

    private final Integer code;
}
