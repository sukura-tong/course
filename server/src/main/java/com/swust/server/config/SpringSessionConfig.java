//package com.swust.server.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SpringSessionConfig {
//    // 最新的chrome，设置null会默认成lax 但是如果设置samesite为NONE,又需要设置secure。https支持secure，http不行
//    @Bean
//    public CookieSerializer httpSessionIdResolver() {
//        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
//        cookieSerializer.setUseHttpOnlyCookie(false);
//        cookieSerializer.setSameSite("None");
//        cookieSerializer.setCookiePath("/");
//        cookieSerializer.setUseSecureCookie(true);
//        return cookieSerializer;
//    }
//}
