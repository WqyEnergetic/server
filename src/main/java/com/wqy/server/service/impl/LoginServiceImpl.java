package com.wqy.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqy.server.dto.UserDTO;
import com.wqy.server.mapper.UserMapper;
import com.wqy.server.pojo.User;
import com.wqy.server.service.LoginService;
import com.wqy.server.utils.SendVerifyCodeUtil;
import com.wqy.server.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.wqy.server.constant.RedisConstants.*;
import static com.wqy.server.enums.status.MessageInfo.FAIL_LOGIN;
import static com.wqy.server.enums.status.MessageInfo.SUCCESS_LOGIN;
import static com.wqy.server.enums.status.StatusCode.OUT_OF_LOGIN;

/**
 * @author: wqy
 * @description: 登录服务
 * @version: 1.0
 * @date: 2023/8/5 22:06
 */
@Service
@Slf4j
@Transactional
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发送验证码
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String getVCode(UserDTO user) throws Exception {

        log.info("进入getVCode方法");
        log.info("user.getPhoneNumber()={}", user.getPhoneNumber());

        //生成验证码
        String verifyCode = ((Integer) RandomUtil.randomInt(100000, 999999)).toString();
        log.info("生成的验证码={}", verifyCode);

        //保存验证码到redis(PhoneNumber作为key，verifyCode作为value)
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + user.getPhoneNumber(),
                verifyCode, LOGIN_CODE_TTL, TimeUnit.MINUTES);

        //发送验证码
        SendVerifyCodeUtil.send(user.getPhoneNumber(), verifyCode);

        return verifyCode;
    }


    /**
     * 短信验证码登录，注册
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public R login(UserDTO user) {

        Map<String, Object> data = new HashMap<>();

        //判断是否存在空指针异常
        if (user.getPhoneNumber() == null && user.getVerifyCode() == null) {
            return null;
        }
        log.info("进入login方法");

        //获取用户输入的手机号
        String phoneNumber = user.getPhoneNumber();

        //检查验证码是否匹配
        String sessionVerifyCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + user.getPhoneNumber());

        if (!sessionVerifyCode.equals(user.getVerifyCode())) {
            log.info("离开login方法,因为验证码错误");
            return R.error().setData("status", "验证码错误");
        }

        //验证码正确就根据手机号查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "phone_number", "account_money")
                .eq("phone_number", phoneNumber);
        User queryUser = getOne(queryWrapper);
        if (queryUser == null) {
            //用户不存在,创建新用户,保存用户到数据库
            //userTmp是创建的临时对象，是save()的参数
            queryUser = new User();
            queryUser.setPhoneNumber(phoneNumber);
            queryUser.setId(IdUtil.objectId());
            queryUser.setAccountMoney(0);
            queryUser.setDelete(false);
            save(queryUser);
            //第一次登录，设为新账户
            data.put("isNewAccount", true);
            log.info("已保存用户信息到数据库");
        } else {
            //非第一次登录，设为非新账户
            data.put("isNewAccount", false);
        }
        //保存用户信息到redis
        //随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);

        log.info("生成的token:{}", token);

        //将User对象转为Hash存储
        UserDTO userDTO = BeanUtil.copyProperties(queryUser, UserDTO.class);

        //将userDTO中的非String类型的数据转成String
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((k, v) -> v != null ? v.toString() : null)
        );

        //存储
        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY + token, userMap);
        stringRedisTemplate.expire(LOGIN_USER_KEY + token, LOGIN_USER_TTL, TimeUnit.MINUTES);
        data.put("token", token);
        log.info("离开login方法");
        return R.ok().setData(data);
    }

    /**
     * 用户退出登录
     *
     * @return
     */
    @Override
    public R exitLogin(String token) {
        log.info(token);
        //从redis缓存中删除用户登录信息
        stringRedisTemplate.expire(LOGIN_USER_KEY + token, 0, TimeUnit.MINUTES);
        log.info("已退出登录");
        return R.ok();
    }

    /**
     * 用户是否登录
     *
     * @param token
     * @return
     */
    @Override
    public R isLogin(String token) {
        log.info("token={}", token);
        String id = (String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "id");
        if (StrUtil.isBlank(id)) {
            log.info("用户不处于登录状态");
            //用户不处于登录状态
            return R.error()
                    .setCode(OUT_OF_LOGIN.getCode())
                    .setMessage(FAIL_LOGIN.getType());
        }
        //用户处于登录状态
        //将登录状态延长30min
        log.info("用户处于登录状态");
        stringRedisTemplate.expire(LOGIN_USER_KEY + token, LOGIN_USER_TTL, TimeUnit.MINUTES);
        return R.ok().setMessage(SUCCESS_LOGIN.getType());
    }

    /**
     * 用户首次登录初始化密码
     *
     * @param token
     * @param data
     * @return
     */
    @Override
    public R initPwd(String token, Map<String, Object> data) {
        log.info("进入 initPwd 方法");

        //从data map中获取用户密码
        String password = data.get("password").toString();
        //从redis中获取userId
        String userId = (String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "id");

        //将密码写入用户信息中
        User user = new User();
        user.setPassword(password);
        user.setId(userId);

        if (updateById(user)) {
            log.info("离开 initPwd 方法");
            return R.ok();
        } else {
            log.info("离开 initPwd 方法");
            return R.error();
        }
    }
}
