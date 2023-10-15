package com.wqy.server.service;

import com.wqy.server.vo.R;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/8/12 10:27
 */
public interface SettingsService {


    /**
     * 设置登录密码
     *
     * @param data 用户输入的密码
     * @return
     */
    R setPwd(Map<String, Object> data);

    /**
     * 注销账号
     *
     * @return
     */
    R deleteAccount(String token);

    /**
     * 保存身份证正反面图片
     *
     * @param token
     * @param frontIdCardFile 身份证正面图片
     * @param backIdCardFile  身份证反面图片
     * @return
     */
    R saveUserIdCardImg(String token, MultipartFile frontIdCardFile, MultipartFile backIdCardFile) throws IOException;

    /**
     * 保存驾驶证正反面图片
     *
     * @param token
     * @param frontDLFile 身份证正面图片
     * @param backDLFile  身份证反面图片
     * @return
     */
    R saveDLImg(String token, MultipartFile frontDLFile, MultipartFile backDLFile) throws IOException;

    /**
     * 获取用于修改手机号的验证码
     *
     * @param phoneNumber
     * @param token
     * @return
     * @throws Exception
     */
    R getVCode(String phoneNumber, String token) throws Exception;

    /**
     * 修改手机号码
     *
     * @param phoneNumber
     * @param verifyCode
     * @param token
     * @return
     */
    R modify(String phoneNumber, String verifyCode, String token);

    R sendVCodeToOriginalPhoneNum(String token) throws Exception;

    R verify(String verifyCode, String token);
}
