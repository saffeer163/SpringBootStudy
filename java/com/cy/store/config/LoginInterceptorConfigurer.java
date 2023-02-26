package com.cy.store.config;

import com.cy.store.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理器拦截器的注册
 */
@Configuration //加载当前的拦截器并进行注册
public class LoginInterceptorConfigurer implements WebMvcConfigurer {
	//配置拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//创建自定义的拦截器对象
		HandlerInterceptor interceptor = new LoginInterceptor();
		//配置白名单:存放在List集合中
		List<String> patterns = new ArrayList<>();
		patterns.add("/bootstrap3/**");
		patterns.add("/css/**");
		patterns.add("/images/**");
		patterns.add("/js/**");
		patterns.add("/web/login.html");
		patterns.add("/web/register.html");
		patterns.add("/web/product.html");
		patterns.add("/users/**");
		patterns.add("/districts/**");
		patterns.add("/products/**");
		patterns.add("/carts/**");
		patterns.add("/addresses/**");
		
		//完成拦截器的注册
		registry.addInterceptor(interceptor)
				.addPathPatterns("/**")//添加拦截名单 /**表示该目录下所有
				.excludePathPatterns(patterns);//添加白名单
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//资源映射路径
		registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:E:/StoreProject/upload/");
		
		registry.addResourceHandler("/pruduct/**")
				.addResourceLocations("file:E:/StoreProject/product/");
	}
}
