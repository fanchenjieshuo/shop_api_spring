package com.fh.common.config;


import com.fh.common.interceptor.KuaYuInterceptor;
import com.fh.common.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new KuaYuInterceptor());

        registration.addPathPatterns("/**");                      //所有路径都被拦截
        InterceptorRegistration registration1 = registry.addInterceptor(new LoginInterceptor());
        registration1.addPathPatterns("/productCartController/**");
        registration1.addPathPatterns("/areaController/**");
        registration1.addPathPatterns("/orderController/**");
    }


}
