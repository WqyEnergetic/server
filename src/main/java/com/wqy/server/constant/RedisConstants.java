package com.wqy.server.constant;

/**
 * @author: wqy
 * @description: redis有关常量
 * @version: 1.0
 * @date: 2023/8/10 11:06
 */
public class RedisConstants {

    public static final String LOGIN_CODE_KEY = "login:code:";

    public static final Long LOGIN_CODE_TTL = 2L;

    public static final String LOGIN_USER_KEY = "login:token:";

    public static final Long LOGIN_USER_TTL = 30L;

    public static final String MODIFY_PHONE_NUMBER = "modify_phone_number:code:";

    public static final String ORIGINAL_PHONE_NUMBER_VERIFY = "original_phone_number:verify:";

}
