package com.swust;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(EurekaApplication.class);

//    public static void main(String[] args) {
//        SpringApplication.run(EurekaApplication.class, args);
//    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(EurekaApplication.class);
        Environment environment = application.run(args).getEnvironment();
        LOGGER.info("start success !!!");
        LOGGER.info("Eureka address is : \t http://127.0.0.1:{}",environment.getProperty("server.port"));
    }
}
