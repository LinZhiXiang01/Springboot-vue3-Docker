package com.mocha.springboot.config;


import com.mocha.springboot.interceptor.TokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by MoCha on 2016/12/9.
 * 配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //链式编程，addPathPattens()表示拦截所有请求
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").excludePathPatterns("/files/**");
    }
}
