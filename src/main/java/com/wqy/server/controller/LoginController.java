package com.wqy.server.controller;

import com.wqy.server.dto.UserDTO;
import com.wqy.server.service.LoginService;
import com.wqy.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author: wqy
 * @description: 登录控制层
 * @version: 1.0
 * @date: 2023/5/2 11:03
 */
@Api(tags = "登录功能")
@Slf4j
@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

    @Resource
    private LoginService loginService;

    /**
     * 获取验证码
     *
     * @return
     */
    @ApiOperation("获取验证码")
    @PostMapping("/getVCode")
    public R getVCode(@RequestBody UserDTO user) throws Exception {
        return R.ok().setData("verifyCode", loginService.getVCode(user));
    }

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @ApiOperation("进行登录")
    @PostMapping("/storeInfoToDB")
    public R storeInfoToDB(@RequestBody UserDTO user) {
        return loginService.login(user);
    }

    /**
     * 前端用户传来的token
     *
     * @param token
     * @return
     */
    @ApiOperation("退出登录")
    @GetMapping("/exit/{token}")
    public R exit(@PathVariable String token) {
        System.out.println("token=" + token);
        return loginService.exitLogin(token);
    }

    /**
     * 获取登录状态
     *
     * @param token
     * @return
     */
    @ApiOperation("获取登录状态")
    @GetMapping("/isLogin/{token}")
    public R checkLoginStatus(@PathVariable String token) {
        return loginService.isLogin(token);
    }

    /**
     * 设置首次登录的密码
     *
     * @param token
     * @param data
     * @return
     */
    @ApiOperation("设置首次登录的密码")
    @PostMapping("/initPwd/{token}")
    public R initPwd(@PathVariable String token,
                     @RequestBody Map<String, Object> data) {
        return loginService.initPwd(token, data);
    }


}
