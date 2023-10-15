package com.wqy.server.controller;

import com.wqy.server.service.SettingsService;
import com.wqy.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @author: wqy
 * @description: 设置功能的控制层
 * @version: 1.0
 * @date: 2023/8/11 17:23
 */
@Api(tags = "设置功能")
@RestController
@RequestMapping("/settings")
@Slf4j
@CrossOrigin
public class SettingsController {

    @Resource
    private SettingsService settingsService;

    /**
     * 设置登录密码
     *
     * @param data 用户数据
     * @return
     */
    @ApiOperation("设置登录密码")
    @PostMapping("/setPassword")
    public R setPassword(@RequestBody Map<String, Object> data) {
        return settingsService.setPwd(data);
    }

    /**
     * 删除账户
     *
     * @param token
     * @return
     */
    @ApiOperation("删除账户")
    @GetMapping("/deleteAccount/{token}")
    public R deleteAccount(@PathVariable String token) {
        return settingsService.deleteAccount(token);
    }

    /**
     * 保存身份证正反面图片
     *
     * @param token
     * @param frontIdCardFile 身份证正面图片
     * @param backIdCardFile  身份证反面图片
     * @return
     */
    @ApiOperation("保存身份证正反面图片")
    @PostMapping("/saveUserIdCardImg/{token}")
    public R saveUserIdCardImg(@PathVariable String token,
                               @RequestParam("frontIdCardFile") MultipartFile frontIdCardFile,
                               @RequestParam("backIdCardFile") MultipartFile backIdCardFile) throws IOException {
        return settingsService.saveUserIdCardImg(token, frontIdCardFile, backIdCardFile);
    }

    /**
     * 保存驾驶证正反面图片
     *
     * @param token
     * @param frontDLFile 身份证正面图片
     * @param backDLFile  身份证反面图片
     * @return
     */
    @ApiOperation("保存身份证正反面图片")
    @PostMapping("/saveDLImg/{token}")
    public R saveUserDLImg(@PathVariable String token,
                           @RequestParam("frontDLFile") MultipartFile frontDLFile,
                           @RequestParam("backDLFile") MultipartFile backDLFile) throws IOException {
        return settingsService.saveDLImg(token, frontDLFile, backDLFile);
    }

    /**
     * 向原手机号发送验证码
     *
     * @param token
     * @return
     */
    @ApiOperation("向原手机号发送验证码")
    @GetMapping("/originalPhoneNumVCode/{token}")
    public R originalPhoneNumVCode(@PathVariable String token) throws Exception {
        return settingsService.sendVCodeToOriginalPhoneNum(token);
    }

    /**
     * 核验原手机号的验证码
     *
     * @param map 里面有 VerifyCode 属性
     * @return
     */
    @ApiOperation("核验原手机号的验证码")
    @PostMapping("/verifyOriginalPhoneNumber/{token}")
    public R verifyOriginalPhoneNumber(@RequestBody Map<String, Object> map,
                                       @PathVariable String token) {
        String verifyCode = map.get("verifyCode").toString();
        return settingsService.verify(verifyCode,token);
    }


    /**
     * 获取用于修改手机号的验证码
     *
     * @param data
     * @return
     */
    @ApiOperation("获取用于修改手机号的验证码")
    @PostMapping("/getModifyVCode/{token}")
    public R getModifyVCode(@RequestBody Map<String, Object> data, @PathVariable String token) throws Exception {
        //从data中得到用户的modifyPhoneNumber
        String phoneNumber = data.get("phoneNumber").toString();
        return settingsService.getVCode(phoneNumber, token);
    }


    /**
     * 修改手机号码
     *
     * @param data  phoneNumber + verifyCode
     * @param token
     * @return
     */
    @ApiOperation("修改手机号码")
    @PostMapping("/modifyPhoneNumber/{token}")
    public R modifyPhoneNumber(@RequestBody Map<String, Object> data,
                               @PathVariable String token) {
        String phoneNumber = data.get("phoneNumber").toString();
        String verifyCode = data.get("verifyCode").toString();
        return settingsService.modify(phoneNumber, verifyCode, token);
    }

}
