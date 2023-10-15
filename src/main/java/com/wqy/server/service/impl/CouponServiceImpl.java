package com.wqy.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqy.server.mapper.CouponMapper;
import com.wqy.server.pojo.Coupon;
import com.wqy.server.service.CouponService;
import com.wqy.server.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.wqy.server.constant.RedisConstants.LOGIN_USER_KEY;
import static com.wqy.server.enums.status.StatusCode.OUT_OF_LOGIN;
import static com.wqy.server.enums.status.StatusCode.SUCCESS;

/**
 * @author: wqy
 * @description: 实现优惠券功能
 * @version: 1.0
 * @date: 2023/7/31 9:18
 */
@Service
@Slf4j
@Transactional
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public R getCoupons(String status, String token) {
        log.info("进入getCoupons方法");

        //从缓存中查找用户信息(id)
        String userId = (String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "id");

        if (userId == null) {
            //缓存中没有没查到就返回空集合
            return R.error()
                    .setCode(OUT_OF_LOGIN.getCode())
                    .setData(new ArrayList<>());
        }

        //封装查询条件并查询
        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status)
                .eq("user_id", userId);
        List<Coupon> list = list(queryWrapper);

        if (list.size() == 0) {
            log.info("未查询到数据");
            log.info("离开getCoupons方法");
            return R.ok().setData(new ArrayList<>());
        }

        log.info("离开getCoupons方法");
        return R.ok().setCode(SUCCESS.getCode()).setData(list);
    }
}
