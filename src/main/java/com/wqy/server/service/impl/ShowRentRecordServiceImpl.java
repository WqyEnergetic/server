package com.wqy.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqy.server.enums.RentStatus;
import com.wqy.server.mapper.TripRecordMapper;
import com.wqy.server.pojo.Trip;
import com.wqy.server.service.ShowRentRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.wqy.server.constant.RedisConstants.LOGIN_USER_KEY;

/**
 * @author: wqy
 * @description: 提供所有租车信息的服务
 * @version: 1.0
 * @date: 2023/7/27 11:36
 */
@Slf4j
@Service
@Transactional
public class ShowRentRecordServiceImpl extends ServiceImpl<TripRecordMapper, Trip> implements ShowRentRecordService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 构建通用查询条件
     *
     * @param token token
     * @param rentStatus rentStatus
     * @return
     */
    private QueryWrapper<Trip> rentRecordWrapper(String token, String rentStatus) {
        String userId = (String) stringRedisTemplate.opsForHash()
                .get(LOGIN_USER_KEY + token, "id");

        QueryWrapper<Trip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", rentStatus)
                .eq("user_id", userId);

        return queryWrapper;
    }

    /**
     * 查找完成订单
     *
     * @return
     */
    @Override
    public List<Trip> showFinished(String token) {
        log.info("进入showFinished方法");

        QueryWrapper<Trip> queryWrapper = rentRecordWrapper(token, RentStatus.RENT_FINISHED.getType());

        List<Trip> list = list(queryWrapper);
        if (list.size() == 0) {
            log.info("未查询到数据");
            log.info("离开showFinished方法");
            return null;
        }
        log.info("离开showFinished方法");
        return list;
    }

    /**
     * 查找未出行订单
     *
     * @return
     */
    @Override
    public List<Trip> showWillDo(String token) {
        log.info("进入showWillDo方法");
        QueryWrapper<Trip> queryWrapper = rentRecordWrapper(token, RentStatus.NOT_TRAVEL.getType());
        List<Trip> list = list(queryWrapper);
        if (list.size() == 0) {
            log.info("未查询到数据");
            log.info("离开showWillDo方法");
            return null;
        }
        log.info("离开showWillDo方法");
        return list;
    }

    /**
     * 查找正在进行中订单
     *
     * @return
     */
    @Override
    public List<Trip> showRenting(String token) {
        log.info("进入showRenting方法");
        QueryWrapper<Trip> queryWrapper = rentRecordWrapper(token, RentStatus.RENTING.getType());
        List<Trip> list = list(queryWrapper);
        if (list.size() == 0) {
            log.info("未查询到数据");
            log.info("离开showRenting方法");
            return null;
        }
        log.info("离开showRenting方法");
        return list;
    }

}
