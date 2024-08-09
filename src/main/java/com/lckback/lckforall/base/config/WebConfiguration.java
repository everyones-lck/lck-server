package com.lckback.lckforall.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry corsRegistry) {
		corsRegistry.addMapping("/**")
			.allowedOrigins("*")
			.exposedHeaders("Authorization")
			.allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE")
			.maxAge(3600);
	}

}
