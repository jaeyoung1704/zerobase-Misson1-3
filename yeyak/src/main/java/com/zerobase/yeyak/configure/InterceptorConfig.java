package com.zerobase.yeyak.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zerobase.yeyak.intercepter.LoginInterceptor;

//인터셉터 등록
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
	registry.addInterceptor(new LoginInterceptor())
	    .addPathPatterns("/customer/*")
	    .addPathPatterns("/customer/*/*")
	    .addPathPatterns("/manager/*")
	    .addPathPatterns("/manager/*/*")
	    .addPathPatterns("/kiosk/*")
	    .addPathPatterns("/kiosk/*/*");

    }

}
