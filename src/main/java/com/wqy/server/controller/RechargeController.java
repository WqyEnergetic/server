package com.wqy.server.controller;

import com.google.gson.Gson;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wqy.server.service.RechargeService;
import com.wqy.server.utils.HttpUtils;
import com.wqy.server.utils.WechatPay2ValidatorForRequest;
import com.wqy.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: wqy
 * @description: 充值功能
 * @version: 1.0
 * @date: 2023/8/11 17:11
 */
@Api(tags = "充值功能")
@Slf4j
@RestController
@RequestMapping("/recharge")
@CrossOrigin
public class RechargeController {

    @Resource
    private RechargeService rechargeService;

    @Resource
    private Verifier verifier;

    /**
     * 获取用户余额
     *
     * @param data
     * @return
     */
    @ApiOperation("获取用户余额")
    @PostMapping("/getMoney")
    public R showAccountMoney(@RequestBody Map<String, Object> data) {
        String token = (String) data.get("token");
        return rechargeService.showUserMoney(token);
    }

    /**
     * 通过微信支付进行充值
     *
     * @param data 用户的token和选择充钱的金额
     * @return
     */
    @ApiOperation("微信支付")
    @PostMapping("/wechat-pay")
    public R wechatPay(@RequestBody Map<String, Object> data) throws IOException {
        log.info("data={}",data);
        return rechargeService.payWithWeChat(data);
    }

    @ApiOperation("微信支付回调通知")
    @PostMapping("/notify")
    public String nativeNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<>();//应答对象

        //处理通知参数
        String body = HttpUtils.readData(request);
        HashMap<String, Object> bodyMap = gson.fromJson(body, HashMap.class);
        String requestId = (String)bodyMap.get("id");
        log.info("支付通知的id：--> {}", bodyMap.get("id"));
        log.info("支付通知的完整数据：--> {}", body);

        //签名的验证
        WechatPay2ValidatorForRequest validator
                = new WechatPay2ValidatorForRequest(verifier,requestId ,body);
        if (!validator.validate(request)) {
            log.error("通知验签失败");
            //失败应答
            response.setStatus(500);
            map.put("code", "ERROR");
            map.put("message", "通知验签失败");
            return gson.toJson(map);
        }
        log.info("通知验签成功");

        //处理订单
        rechargeService.processOrder(bodyMap);

        //成功应答
        response.setStatus(200);
        map.put("code", "SUCCESS");
        map.put("message", "成功");

        return gson.toJson(map);
    }

    @ApiOperation("获取支付状态")
    @PostMapping("/pay-status")
    public R getPayStatus(@RequestBody Map<String, Object> data){
        log.info("data={}",data);
        return rechargeService.AfterSuccessPay(data);
    }

}
