package com.abdullayevtural.book.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Açıq şəkildə /frontend/** üçün statik faylları veririk
		registry.addResourceHandler("/frontend/**")
				.addResourceLocations("classpath:/static/frontend/")
				.setCachePeriod(0);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// http://localhost:8020/ açılanda birbaşa frontend səhifəsinə yönləndir
		registry.addViewController("/").setViewName("redirect:/frontend/index.html");
	}
}
