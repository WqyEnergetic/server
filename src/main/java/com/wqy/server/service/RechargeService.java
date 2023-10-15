package com.wqy.server.service;

import com.wqy.server.vo.R;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: wqy
 * @description: 充值功能接口
 * @version: 1.0
 * @date: 2023/8/14 14:26
 */
public interface RechargeService {

    /**
     * 查询用户余额并返回到前端
     *
     * @param token
     * @return
     */
    R showUserMoney(String token);

    /**
     * 用户选择微信支付
     *
     * @param data 用户提交的数据
     * @return
     */
    R payWithWeChat(Map<String, Object> data) throws IOException;

    /**
     * 处理订单
     *
     * @param bodyMap
     */
    void processOrder(HashMap<String, Object> bodyMap) throws GeneralSecurityException;

    /**
     *
     * @param data
     * @return
     */
    R AfterSuccessPay(Map<String, Object> data);
}
