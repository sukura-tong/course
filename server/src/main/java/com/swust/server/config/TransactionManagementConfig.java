package com.swust.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 雪瞳
 * @Slogan 忘时，忘物，忘我。
 * @Function
 * spring事务配置类
 */
@EnableTransactionManagement//标识开启事务支持
@Configuration// 标识配置类
public class TransactionManagementConfig {
}
