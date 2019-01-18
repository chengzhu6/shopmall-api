package com.castellan.common;

import com.castellan.filter.AutoLoginFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Autowired
    private AutoLoginFilter autoLoginFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 可添加多个
        registry.addInterceptor(autoLoginFilter).addPathPatterns(Const.FilterConst.NEED_LOGIN_PATH);
    }

}
