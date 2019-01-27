package com.castellan.common;

import com.castellan.filter.AutoLoginFilter;

import com.castellan.filter.RoleCheckFilter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootConfiguration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Autowired
    private AutoLoginFilter autoLoginFilter;

    @Autowired
    private RoleCheckFilter roleCheckFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 可添加多个
        List<String> excludes = Lists.newArrayList();
        excludes.add("/product/**");
        excludes.add("/user/login.do");
        excludes.add("/back/**");
        registry.addInterceptor(autoLoginFilter).addPathPatterns("/**").excludePathPatterns(excludes);
        registry.addInterceptor(roleCheckFilter).addPathPatterns("/back/**").excludePathPatterns("/back/login.do");
    }

}
