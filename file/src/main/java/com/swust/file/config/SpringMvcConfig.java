package com.swust.file.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 雪瞳
 * @Slogan 忘时，忘物，忘我。
 * @Function
 * 静态资源配置类
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //所有带有/f的请求都是静态资源
        registry.addResourceHandler("/f/**")
                .addResourceLocations("file:F:/项目代码/file/swust/course/");
    }
}
