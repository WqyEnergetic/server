package com.wqy.server.service;

import com.wqy.server.pojo.Trip;
import com.wqy.server.vo.R;

import java.util.Map;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/8/21 13:40
 */
public interface GenerateRentOrderService {

    /**
     * 计算最终租金价格
     *
     * @param data
     * @return
     */
    R getTotalRentCarPrice(Map<String, Object> data);

    /**
     * 用户支付租金
     *
     * @param trip
     * @return
     */
    R payBill(Trip trip, String token);

    /**
     * 检查用户支付密码
     *
     * @param data password和token
     * @return
     */
    R checkPwd(Map<String, Object> data);
}
