package com.wqy.server.enums.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/7/22 19:55
 */
@Getter
@AllArgsConstructor
public enum MessageInfo {

    SELECT_SUCCESS("查询成功"),

    SELECT_FAIL("查询失败"),

    FAIL_LOGIN("登录失效"),

    SUCCESS_LOGIN("登录成功"),

    VERIFY_CODE_ERROR("验证码错误"),

    PHONE_NUM_SUCCESS_MODIFY("手机号修改成功"),

    DUPLICATE_PHONE_NUM("手机号冲突");

    private final String type;
}
