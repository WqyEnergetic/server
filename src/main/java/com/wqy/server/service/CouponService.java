package com.wqy.server.service;

import com.wqy.server.pojo.Coupon;
import com.wqy.server.vo.R;

import java.util.List;

/**
 * @author: wqy
 * @description: 实现优惠券功能接口
 * @version: 1.0
 * @date: 2023/7/31 9:18
 */
public interface CouponService {

    /**
     * 根据status得到所需优惠券
     *
     * @param status
     * @return
     */
    R getCoupons(String status, String token);
}
