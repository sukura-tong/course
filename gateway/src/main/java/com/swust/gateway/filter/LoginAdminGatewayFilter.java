package com.swust.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Component
public class LoginAdminGatewayFilter implements GatewayFilter, Ordered {

    private static final Logger LOG = LoggerFactory.getLogger(LoginAdminGatewayFilter.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求地址
        String path = exchange.getRequest().getURI().getPath();
        // 如果请求不包含admin则代表是非控制台请求，不需要拦截
        if (!path.contains("/admin/")){
            return chain.filter(exchange);
        }
        // 排除登录、退出、验证码三个请求、均不需要拦截
        if (path.contains("/system/admin/kaptcha/imagecode")||
        path.contains("/system/admin/user/userlogin")
        ||path.contains("/system/admin/user/userloginout")){
            LOG.info("该请求{}不需要拦截",path);
            return chain.filter(exchange);
        }
        //获取header的token参数 开始拦截验证
        String token = exchange.getRequest().getHeaders().getFirst("token");
        LOG.info("登陆拦截开始{}",token);
        if (token == null || token.isEmpty()){
            LOG.info("不包含token信息，已拦截该请求");
            // 401 未授权
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        // 从redis获取token对应信息
        Object info = redisTemplate.opsForValue().get(token);
        if (info == null){
            LOG.info("token已过期，已拦截该请求");
            // 401 未授权
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }else {
            LOG.info("登陆验证通过");
            // 判断接口权限是否需要拦截
            LOG.info("接口权限校验，请求地址:{}",path);
            boolean exist = false;
            JSONObject loginUserDto = JSON.parseObject(String.valueOf(info));
            JSONArray requests = loginUserDto.getJSONArray("requests");

            // 遍历是否满足请求权限
            for (int i = 0; i < requests.size(); i++){
                String request = (String) requests.get(i);
                if (path.contains(request)){
                    exist = true;
                    break;
                }
            }

            if (exist){
                LOG.info("接口验证通过");
            }else {
                LOG.info("接口验证失败");
                LOG.info("该请求已拒绝");
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder()
    {
        return 1;
    }
}