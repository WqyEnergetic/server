package com.wqy.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wqy.server.enums.OrderStatus;
import com.wqy.server.enums.status.StatusCode;
import com.wqy.server.mapper.CarMapper;
import com.wqy.server.mapper.TripRecordMapper;
import com.wqy.server.mapper.UserMapper;
import com.wqy.server.pojo.Car;
import com.wqy.server.pojo.Trip;
import com.wqy.server.pojo.User;
import com.wqy.server.service.GenerateRentOrderService;
import com.wqy.server.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static com.wqy.server.constant.RedisConstants.LOGIN_USER_KEY;
import static com.wqy.server.enums.status.StatusCode.NOT_ENOUGH_MONEY;
import static com.wqy.server.enums.status.StatusCode.SUCCESS_PAY;

/**
 * @author: wqy
 * @description: 生成租车订单服务
 * @version: 1.0
 * @date: 2023/8/21 13:40
 */

@Service
@Slf4j
@Transactional
public class GenerateRentOrderServiceImpl implements GenerateRentOrderService {

    @Resource
    private CarMapper carMapper;

    @Resource
    private TripRecordMapper tripRecordMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 得到总天数
     *
     * @param timeRange 时间范围
     * @return
     */
    private int getTotalDays(String timeRange) {
        String[] splitStr = timeRange.split("--|~");
        String startTime = splitStr[0].trim();
        String endTime = splitStr[1].trim();

        String[] formatStrings = {
                "yyyy-MM-dd H:mm",
                "yyyy-MM-dd HH:mm",
                "yyyy-M-d H:mm",
                "yyyy-MM-d H:mm",
                "yyyy-MM-d HH:mm"
        };
        DateTimeFormatter formatter = null;
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        for (String formatString : formatStrings) {
            try {
                formatter = DateTimeFormatter.ofPattern(formatString);
                startDateTime = LocalDateTime.parse(startTime, formatter);
                endDateTime = LocalDateTime.parse(endTime, formatter);
                break;
            } catch (Exception e) {

            }
        }

        if (formatter == null) {
            throw new IllegalArgumentException("Invalid date format");
        }

        long minutesBetween = ChronoUnit.MINUTES.between(startDateTime, endDateTime);

        int daysBetween = (int) Math.ceil(minutesBetween / (24.0 * 60.0));

        return daysBetween;
    }

    /**
     * 生成行程信息
     *
     * @param trip
     * @param userId
     */
    private Trip generateTripRecord(Trip trip, String userId) {
        trip.setUserId(userId);
        trip.setStatus("未出行");
        return trip;
    }

    @Override
    public R getTotalRentCarPrice(Map<String, Object> data) {
        log.info("data={}", data);
        //解析前端数据
        String timeRange = (String) data.get("time");
        String carId = (String) data.get("carId");

        //从timeRange中解析出租赁天数
        int days = getTotalDays(timeRange);

        //从数据库中得到车的每日的价格
        Car car = carMapper.selectOne(
                new QueryWrapper<Car>().select("rent_price").eq("id", carId)
        );

        //是否是顺风车
        if (data.get("carpooling") != null && (Boolean) data.get("carpooling") == true) {
            return R.ok().setData("price", car.getRentPrice());
        }

        if (car == null) {
            return R.error();
        }
        return R.ok().setData("price", days * car.getRentPrice());
    }

    @Override
    public R payBill(Trip trip, String token) {
        log.info("trip={},token={}", trip, token);
        //1:先获取用户余额 accountMoney，再获取用户id
        String money = (String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "accountMoney");
        String userId = (String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "id");

        Integer accountMoney = Integer.parseInt(money);
        Integer price = trip.getRentPrice();
        //2:将用户要支付的price与accountMoney进行比较
        if (accountMoney < price) {
            //2.1：若 price < accountMoney,就返回201
            return R.error()
                    .setCode(NOT_ENOUGH_MONEY.getCode())
                    .setMessage("余额不足");
        } else {
            //2.2：若 price >= accountMoney
            //2.2.1: 使用UserMapper，将数据库中的字段 account_money 更新为 原来的account_money值减去price
            User user = new User();
            user.setId(userId);
            user.setAccountMoney(accountMoney - price);
            userMapper.updateById(user);

            //2.2.2: 将redis中的字段 accountMoney 更新为 原来的accountMoney值减去price
            stringRedisTemplate.opsForHash().put(LOGIN_USER_KEY + token, "accountMoney", String.valueOf(user.getAccountMoney()));

            //3:更新订单信息
            tripRecordMapper.insert(generateTripRecord(trip, userId));

            //4.返回101
            return R.ok()
                    .setCode(SUCCESS_PAY.getCode())
                    .setMessage(OrderStatus.SUCCESS.getType());
        }
    }

    /**
     * 检查用户输入的密码
     *
     * @param data password和token
     * @return
     */
    @Override
    public R checkPwd(Map<String, Object> data) {
        String password = (String) data.get("password");
        String token = (String) data.get("token");

        //通过token获取用户id
        String userId = (String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "id");

        //数据库里匹配密码
        User user = userMapper.selectById(userId);
        if (user == null) {
            return R.error();
        }
        //匹配密码
        if (!user.getPassword().equals(password)) {
            return R.error()
                    .setCode(StatusCode.PASSWORD_ERROR.getCode())
                    .setMessage("密码错误");
        }
        return R.ok();
    }

}
