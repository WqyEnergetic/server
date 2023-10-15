package com.wqy.server.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqy.server.enums.status.MessageInfo;
import com.wqy.server.enums.status.StatusCode;
import com.wqy.server.mapper.UserMapper;
import com.wqy.server.pojo.User;
import com.wqy.server.service.SettingsService;
import com.wqy.server.utils.SendVerifyCodeUtil;
import com.wqy.server.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.wqy.server.constant.RedisConstants.*;

/**
 * @author: wqy
 * @description: 设置功能的服务
 * @version: 1.0
 * @date: 2023/8/12 10:28
 */
@Service
@Slf4j
@Transactional
public class SettingsServiceImpl extends ServiceImpl<UserMapper, User> implements SettingsService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static String prefixPath = "D:\\cache\\";

    @Override
    public R setPwd(Map<String, Object> data) {
        String password = (String) data.get("password");
        String token = (String) data.get("token");
        String userId = (String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "id");
        //封装修改条件
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("password", password)
                .eq("id", userId)
                .eq("is_delete", false);
        //进行修改
        if (update(updateWrapper)) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @Override
    public R deleteAccount(String token) {
        //1.将数据库中的is_delete属性变为true
        String userId = (String) stringRedisTemplate.opsForHash()
                .get(LOGIN_USER_KEY + token, "id");

        update(null, new UpdateWrapper<User>()
                .set("is_delete", true)
                .eq("id", userId)
        );

        //2.删除redis中相关用户信息
        stringRedisTemplate.expire(LOGIN_USER_KEY + token, 0, TimeUnit.MINUTES);

        return R.ok();
    }

    @Override
    public R saveUserIdCardImg(String token,
                               MultipartFile frontIdCardFile,
                               MultipartFile backIdCardFile) throws IOException {
        String userId = (String) stringRedisTemplate.opsForHash()
                .get(LOGIN_USER_KEY + token, "id");

        //判断文件是否为空
        if (!frontIdCardFile.isEmpty() && !backIdCardFile.isEmpty()) {
            //保存身份证前页
            // 构建文件路径
            String frontIdCardFilePath = prefixPath + userId + "_" + "身份证正面.jpg";
            // 创建文件对象
            File frontFile = new File(frontIdCardFilePath);
            // 保存文件到本地文件系统
            frontIdCardFile.transferTo(frontFile);

            //保存身份证后页
            String backIdCardFilePath = prefixPath + userId + "_" + "身份证反面.jpg";
            File backFile = new File(backIdCardFilePath);
            backIdCardFile.transferTo(backFile);

            //路径添加到数据库
            User user = new User();
            user.setId(userId);
            user.setFrontIdCardImagePath(frontIdCardFilePath);
            user.setBackIdCardImagePath(backIdCardFilePath);
            updateById(user);

            return R.ok();
        }
        return R.error();
    }

    @Override
    public R saveDLImg(String token,
                       MultipartFile frontDLFile,
                       MultipartFile backDLFile) throws IOException {
        String userId = (String) stringRedisTemplate.opsForHash()
                .get(LOGIN_USER_KEY + token, "id");

        //判断文件是否为空
        if (!frontDLFile.isEmpty() && !backDLFile.isEmpty()) {
            //保存驾驶证前页
            String frontDLFilePath = prefixPath + userId + "_" + "驾驶证前页.jpg";
            File frontFile = new File(frontDLFilePath);
            frontDLFile.transferTo(frontFile);

            //保存驾驶证后页
            String backDLFilePath = prefixPath + userId + "_" + "驾驶证后页.jpg";
            File backFile = new File(backDLFilePath);
            backDLFile.transferTo(backFile);

            //路径添加到数据库
            User user = new User();
            user.setId(userId);
            user.setFrontDLImagePath(frontDLFilePath);
            user.setBackDLImagePath(backDLFilePath);
            updateById(user);

            return R.ok();
        }
        return R.error();
    }

    @Override
    public R getVCode(String phoneNumber,String token) throws Exception {
        //检查是否有相同的手机号
        if(getOne(new QueryWrapper<User>().eq("phone_number", phoneNumber)) != null){
            return R.error()
                    .setCode(StatusCode.DUPLICATE_PHONE_NUM.getCode())
                    .setMessage(MessageInfo.DUPLICATE_PHONE_NUM.getType());
        }
        //生成验证码
        String verifyCode = ((Integer) RandomUtil.randomInt(100000, 999999)).toString();

        //保存验证码到redis(PhoneNumber作为key，verifyCode作为value)
        stringRedisTemplate.opsForValue().set(MODIFY_PHONE_NUMBER + phoneNumber,
                verifyCode, LOGIN_CODE_TTL, TimeUnit.MINUTES);

        //发送验证码
        SendVerifyCodeUtil.send(phoneNumber, verifyCode);

        return R.ok();
    }

    @Override
    public R modify(String phoneNumber, String verifyCode ,String token) {

        //从redis中获取验证码
        String redisVerifyCode = stringRedisTemplate.opsForValue().get(MODIFY_PHONE_NUMBER + phoneNumber).toString();

        //获取用户id
        String userId = (String) stringRedisTemplate.opsForHash()
                .get(LOGIN_USER_KEY + token, "id");

        if(redisVerifyCode == null){
            return R.error();
        }

        //将redis中的验证码与形参verifyCode进行比较
        //不一致，return R.error();
        if (!redisVerifyCode.equals(verifyCode)) {
            return R.error()
                    .setCode(StatusCode.VERIFY_CODE_ERROR.getCode())
                    .setMessage(MessageInfo.VERIFY_CODE_ERROR.getType());
        }

        //一致，往下进行
        stringRedisTemplate.expire(MODIFY_PHONE_NUMBER + phoneNumber,0,TimeUnit.MINUTES);

        //通过userid从数据库中修改用户手机号
        User user = new User();
        user.setId(userId);
        user.setPhoneNumber(phoneNumber);
        updateById(user);

        //修改redis中的手机号信息，使之与数据库中的保持一致
        stringRedisTemplate.opsForHash().put(LOGIN_USER_KEY + token,"phoneNumber",phoneNumber);
        stringRedisTemplate.expire(LOGIN_USER_KEY + token,LOGIN_USER_TTL,TimeUnit.MINUTES);

        return R.ok().setMessage("修改成功");
    }

    @Override
    public R sendVCodeToOriginalPhoneNum(String token) throws Exception {
        //1.通过token获取用户手机号
        String phoneNumber = stringRedisTemplate.opsForHash()
                .get(LOGIN_USER_KEY + token,"phoneNumber").toString();

        //2.获取验证码
        String verifyCode = ((Integer) RandomUtil.randomInt(100000, 999999)).toString();

        //3.得到手机号后，调用send向用户发送验证码
        SendVerifyCodeUtil.send(phoneNumber,verifyCode);

        //4.验证码存入redis
        stringRedisTemplate.opsForValue().set(ORIGINAL_PHONE_NUMBER_VERIFY + phoneNumber,
                verifyCode,LOGIN_CODE_TTL,TimeUnit.MINUTES);

        stringRedisTemplate.expire(LOGIN_USER_KEY + token,LOGIN_USER_TTL,TimeUnit.MINUTES);

        return R.ok();
    }

    @Override
    public R verify(String verifyCode, String token) {

        //从redis中获取用户电话号码
        String phoneNumber = stringRedisTemplate.opsForHash()
                .get(LOGIN_USER_KEY + token,"phoneNumber").toString();

        //1.从redis中取出验证码
        String userVerifyCode = stringRedisTemplate.opsForValue().get(ORIGINAL_PHONE_NUMBER_VERIFY + phoneNumber);

        //2.进行比对
        //2.1比对失败，R.error()
        if (!verifyCode.equals(userVerifyCode)) {
            return R.error()
                    .setCode(StatusCode.VERIFY_CODE_ERROR.getCode())
                    .setMessage(MessageInfo.VERIFY_CODE_ERROR.getType());
        }else{
            //2.2比对成功，R.ok()
            return R.ok()
                    .setMessage(MessageInfo.SUCCESS_LOGIN.getType());
        }
    }

}
