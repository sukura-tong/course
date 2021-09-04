package com.swust.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

@SpringBootApplication
//将system模块注册到eureka服务中心
@EnableEurekaClient
public class GatewayApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayApplication.class);


    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GatewayApplication.class);
        Environment environment = application.run(args).getEnvironment();
        LOGGER.info("start success !!!");
        LOGGER.info("System address is : \t http://127.0.0.1:{}",environment.getProperty("server.port"));
    }

    //配置GateWay路由实现跨域操作
    @Bean
    public CorsWebFilter corsWebFilter(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(Boolean.TRUE);
        configuration.addAllowedMethod("*");
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());

        source.registerCorsConfiguration("/**",configuration);
        return new CorsWebFilter(source);
    }
}
