package com.wqy.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqy.server.constant.RedisConstants;
import com.wqy.server.enums.OrderStatus;
import com.wqy.server.mapper.IllegalDrivingMapper;
import com.wqy.server.mapper.UserMapper;
import com.wqy.server.pojo.IllegalDriving;
import com.wqy.server.pojo.User;
import com.wqy.server.service.IllegalDrivingService;
import com.wqy.server.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.wqy.server.constant.RedisConstants.LOGIN_USER_KEY;
import static com.wqy.server.constant.RedisConstants.LOGIN_USER_TTL;
import static com.wqy.server.enums.status.StatusCode.NOT_ENOUGH_MONEY;
import static com.wqy.server.enums.status.StatusCode.SUCCESS_PAY;

/**
 * @author: wqy
 * @description: 违章处理服务的实现
 * @version: 1.0
 * @date: 2023/7/24 13:10
 */
@Service
@Slf4j
@Transactional
public class IllegalDrivingServiceImpl extends ServiceImpl<IllegalDrivingMapper, IllegalDriving> implements IllegalDrivingService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    /**
     * 查询违法记录
     *
     * @param postCode 为1代表客户要查找之前处理的，为0代表查找未处理的
     * @return
     */
    @Override
    public List<IllegalDriving> selectIllegalDrivingRecord(Integer postCode, String token) {

        //从缓存中取出用户信息
        String userId = (String) stringRedisTemplate.opsForHash()
                .get(LOGIN_USER_KEY + token, "id");

        log.info(userId);

        if (userId == null) {
            return null;
        }

        //封装查询条件
        QueryWrapper<IllegalDriving> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deal", postCode)
                .eq("user_id", userId);

        //查询
        List<IllegalDriving> list = list(queryWrapper);


        if (list.size() == 0) {
            return null;
        }

        return list;
    }

    /**
     * 消除违章记录
     *
     * @param data illegalDrivingId + token
     * @return
     */
    @Override
    public R eliminateIllegalRecord(Map<String, Object> data) {
        String illegalDrivingId = (String) data.get("illegalDrivingId");
        String token = (String) data.get("token");

        //1.通过违章id获取金额
        IllegalDriving illegalItem = getById(illegalDrivingId);
        if (illegalItem == null) {
            return R.error();
        }
        Integer fine = illegalItem.getFine();

        //2 从订单余额中扣除相应金额
        String money = (String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "accountMoney");
        String userId = (String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "id");
        Integer accountMoney = Integer.parseInt(money);

        if (accountMoney - fine < 0) {
            //2.1 若扣除金额失败，返回失败状态码
            return R.error()
                    .setCode(NOT_ENOUGH_MONEY.getCode())
                    .setMessage("余额不足");
        } else {
            Integer finalAccountMoney = accountMoney - fine;

            //2.2 扣除金额成功，返回成功状态码
            //改变数据库中的违章记录为已处理
            update(new UpdateWrapper<IllegalDriving>()
                    .set("is_deal",true)
                    .eq("user_id",userId)
                    .eq("id",illegalDrivingId));

            //更新数据库的余额
            userMapper.update(null,new UpdateWrapper<User>()
                    .set("account_money",finalAccountMoney)
                    .eq("id",userId));

            //更新redis的余额字段
            stringRedisTemplate.opsForHash().put(LOGIN_USER_KEY + token,
                    "accountMoney", finalAccountMoney.toString());
            stringRedisTemplate.expire(LOGIN_USER_KEY + token, LOGIN_USER_TTL, TimeUnit.MINUTES);

            return R.ok()
                    .setCode(SUCCESS_PAY.getCode())
                    .setMessage(OrderStatus.SUCCESS.getType());
        }
    }

}
