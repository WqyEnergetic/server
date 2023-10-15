package com.wqy.server.service;

import com.wqy.server.dto.UserDTO;
import com.wqy.server.vo.R;

import java.util.Map;

/**
 * @author: wqy
 * @description: 登录功能接口
 * @version: 1.0
 * @date: 2023/8/5 22:05
 */
public interface LoginService {

    /**
     * 发送验证码
     *
     * @param user 用户信息
     * @return
     */
    String getVCode(UserDTO user) throws Exception;

    /**
     * 短信验证码登录，注册
     *
     * @param user 用户信息
     */
    R login(UserDTO user);

    /**
     * 用户退出登录
     *
     * @return
     */
    R exitLogin(String token);

    /**
     * 用户是否登录
     *
     * @param token
     * @return
     */
    R isLogin(String token);

    /**
     * 用户首次登录初始化密码
     *
     * @param token
     * @param data
     * @return
     */
    R initPwd(String token, Map<String, Object> data);
}
