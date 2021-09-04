package com.swust.file;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
//将system模块注册到eureka服务中心
@EnableEurekaClient
@ComponentScan("com.swust")
@MapperScan("com.swust.server.mapper")
public class FileApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileApplication.class);


    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FileApplication.class);
        Environment environment = application.run(args).getEnvironment();
        LOGGER.info("start success !!!");
        LOGGER.info("file address is : \t http://127.0.0.1:{}",environment.getProperty("server.port"));
    }
}
