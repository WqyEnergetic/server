package com.wqy.server.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wqy.server.config.WxPayConfig;
import com.wqy.server.enums.OrderStatus;
import com.wqy.server.enums.status.StatusCode;
import com.wqy.server.enums.wxpay.WxApiType;
import com.wqy.server.enums.wxpay.WxNotifyType;
import com.wqy.server.mapper.OrderInfoMapper;
import com.wqy.server.mapper.UserMapper;
import com.wqy.server.pojo.OrderInfo;
import com.wqy.server.pojo.User;
import com.wqy.server.service.RechargeService;
import com.wqy.server.utils.JsonStringParseUtil;
import com.wqy.server.utils.OrderNoUtils;
import com.wqy.server.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static com.wqy.server.constant.RedisConstants.LOGIN_USER_KEY;
import static com.wqy.server.enums.OrderStatus.SUCCESS;
import static com.wqy.server.enums.status.MessageInfo.FAIL_LOGIN;
import static com.wqy.server.enums.status.StatusCode.OUT_OF_LOGIN;

/**
 * @author: wqy
 * @description: 充值功能服务
 * @version: 1.0
 * @date: 2023/8/14 14:27
 */
@Service
@Slf4j
@Transactional
public class RechargeServiceImpl extends ServiceImpl<UserMapper, User> implements RechargeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    @Resource
    private WxPayConfig wxPayConfig;

    @Resource
    private CloseableHttpClient wxPayClient;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 生成订单
     *
     * @return 订单信息
     */
    private OrderInfo generateOrder(String userId, Integer money) {

        log.info("开始生成订单");
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("order_status", OrderStatus.NOTPAY.getType());
        //数据库中查找用户是否有未支付订单
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);

        if (orderInfo != null) {
            //用户存在未支付订单,就
            orderInfo.setOrderNo(OrderNoUtils.getOrderNo());
            orderInfo.setTotalFee(money);
            orderInfoMapper.updateById(orderInfo);
        } else {
            //用户不存在未支付订单
            orderInfo = new OrderInfo();
            orderInfo.setTitle("全新租车");
            orderInfo.setOrderStatus(OrderStatus.NOTPAY.getType());
            orderInfo.setOrderNo(OrderNoUtils.getOrderNo());
            orderInfo.setTotalFee(money);//单位:分
            orderInfo.setUserId(userId);
            orderInfo.setOrderStatus(OrderStatus.NOTPAY.getType());
            orderInfoMapper.insert(orderInfo);
        }
        log.info("订单生成完毕：{}", orderInfo);
        return orderInfo;
    }

    /**
     * 密文解密
     *
     * @param bodyMap
     * @return
     * @throws GeneralSecurityException
     */
    private String decryptFromResource(Map<String, Object> bodyMap) throws GeneralSecurityException {
        log.info("密文解密");
        //通知数据
        Map<String, String> resourceMap = (Map) bodyMap.get("resource");
        //数据密文
        String ciphertext = resourceMap.get("ciphertext");
        //随机串
        String nonce = resourceMap.get("nonce");
        //附加数据
        String associateData = resourceMap.get("associated_data");

        log.info("密文-->{}", ciphertext);

        AesUtil aesUtil = new AesUtil(wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        String plainText = aesUtil.decryptToString(
                associateData.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                ciphertext);

        log.info("明文-->{}", plainText);
        return plainText;
    }

    @Override
    public R showUserMoney(String token) {
        //通过redis查询出userId
        String userId = (String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "id");
        //通过userId查询出用户余额
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("account_money")
                .eq("id", userId);
        User user = userMapper.selectOne(queryWrapper);
        //对user做空指针异常判断
        if (user == null) {
            return R.ok().setMessage("用户异常，查询不到id");
        } else {
            return R.ok().setData("accountMoney", user.getAccountMoney());
        }
    }

    @Override
    public R payWithWeChat(Map<String, Object> data) throws IOException {

        //获取userId
        String token = (String) data.get("token");
        String userId = (String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "id");

        if (StrUtil.isBlank(userId)) {
            log.info("登录信息失效");
            //登录信息失效
            return R.error()
                    .setCode(OUT_OF_LOGIN.getCode())
                    .setMessage(FAIL_LOGIN.getType());
        }

        //获取用户充值的金额
        Integer money = Integer.parseInt(data.get("money").toString());

        //生成订单
        OrderInfo orderInfo = generateOrder(userId, money);

        //调用统一下单api
        HttpPost httpPost = new HttpPost(wxPayConfig.getDomain().concat(WxApiType.NATIVE_PAY.getType()));

        // 请求body参数
        Gson gson = new Gson();
        Map paramsMap = new HashMap<>();

        paramsMap.put("appid", wxPayConfig.getAppid());
        paramsMap.put("mchid", wxPayConfig.getMchId());
        paramsMap.put("description", orderInfo.getTitle());
        paramsMap.put("out_trade_no", orderInfo.getOrderNo());
        paramsMap.put("notify_url", wxPayConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));

        Map amountMap = new HashMap<>();
        amountMap.put("total", orderInfo.getTotalFee());
        amountMap.put("currency", "CNY");
        paramsMap.put("amount", amountMap);

        //将参数转换成json字符串
        String jsonParams = gson.toJson(paramsMap);

        StringEntity entity = new StringEntity(jsonParams, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpPost);

        try {
            //响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("响应结果" + statusCode);
            //响应体
            String bodyAsString = EntityUtils.toString(response.getEntity());
            if (statusCode == 200) { //处理成功
                log.info("处理成功" + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("处理成功");
            } else {
                log.info("Native下单失败,错误代码：" + statusCode + ",返回体为:" + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }

            //响应结果
            Map<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);

            //二维码
            String codeUrl = resultMap.get("code_url");

            Map<String, Object> map = new HashMap<>();
            map.put("code_url", codeUrl);
            map.put("orderNo", orderInfo.getOrderNo());//获取订单号

            //将二维码的url保存到数据库中
            orderInfo.setCodeUrl(codeUrl);
            orderInfoMapper.updateById(orderInfo);
            return R.ok().setData(map);

        } finally {
            response.close();
        }
    }

    @Override
    public void processOrder(HashMap<String, Object> bodyMap) throws GeneralSecurityException {
        log.info("处理订单");

        //密文解密
        String plainText = decryptFromResource(bodyMap);

        //将明文转换为map
        Gson gson = new Gson();
        HashMap plainTextMap = gson.fromJson(plainText, HashMap.class);
        String orderNo = (String) plainTextMap.get("out_trade_no");
        Double payerTotal = Double.parseDouble(JsonStringParseUtil.parseTwice(
                plainText, "amount", "payer_total"
        ));
        log.info("payerTotal={}", payerTotal);
        System.out.println(plainTextMap);

        // 尝试获取锁,成功获取则立即返回true，获取失败则立即返回false。不必一直等待锁的释放
        if (lock.tryLock()) {

            //更新订单状态为已支付，并将交易时间加入订单中
            UpdateWrapper<OrderInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("order_status", OrderStatus.SUCCESS.getType())
                    .set("trade_time",plainTextMap.get("success_time"))
                    .eq("order_no", orderNo);

            orderInfoMapper.update(null,updateWrapper);

        }
        //释放锁
        lock.unlock();
    }

    @Override
    public R AfterSuccessPay(Map<String, Object> data) {

        log.info("data={}",data);

        String token = (String) data.get("token");

        //获取用户Id
        String userId = (String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "id");

        //判断是否支付成功
        String status = orderInfoMapper.selectOne(
                new QueryWrapper<OrderInfo>().eq("order_no", data.get("orderNo"))
        ).getOrderStatus();

        log.info("用户未支付");

        if(OrderStatus.NOTPAY.getType().equals(status)){
            return R.error()
                    .setMessage(OrderStatus.NOTPAY.getType())
                    .setCode(StatusCode.NOT_PAY.getCode());
        }

        log.info("用户已支付");

        //更新余额
        //获取未充值前的余额
        Integer accountMoney = Integer.parseInt((String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "accountMoney"));
        //获取充值金额
        Integer rechargedMoney = (Integer) data.get("money");

        log.info("accountMoney={},rechargedMoney={}",accountMoney,rechargedMoney);

        update(new UpdateWrapper<User>()
                .set("account_money",accountMoney + rechargedMoney)
                .eq("id",userId)
        );

        //更新redis里用户的余额
        log.info("更新余额完毕");

        return R.ok()
                .setCode(StatusCode.SUCCESS_PAY.getCode())
                .setMessage(OrderStatus.SUCCESS.getType());
    }

}
