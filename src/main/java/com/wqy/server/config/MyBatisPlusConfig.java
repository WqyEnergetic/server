package com.wqy.server.config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: wqy
 * @description: MyBatisPlus配置类
 * @version: 1.0
 * @date: 2023/8/8 22:31
 */
@Configuration
@MapperScan("com.wqy.server.mapper")
@EnableTransactionManagement//启用事务管理
public class MyBatisPlusConfig {
}

