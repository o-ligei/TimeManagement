package com.oligei.timemanagement.config;

import com.oligei.timemanagement.interceptor.AuthenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePath = new ArrayList<>();
        excludePath.add("/User/LoginWithPassword");
        excludePath.add("/User/LoginWithCaptcha");
        excludePath.add("/User/SendCaptchaToPhone");
        excludePath.add("/User/SendCaptchaToEmail");
        excludePath.add("/User/Register");
        excludePath.add("/User/ResetPassword");
        excludePath.add("/Social/KMeans");
        registry.addInterceptor(new AuthenInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        System.out.println("Finished adding");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
                .maxAge(3600);
    }
}
