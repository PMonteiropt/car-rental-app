package com.car.rental.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

	private static String GET = "GET";
	private static String PUT = "GET";
	private static String POST = "POST";
	private static String DELETE = "DELETE";

	public WebMvcConfigurer corsConfigurer() {

		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {

				registry.addMapping("/**").allowedMethods(GET, PUT, POST, DELETE).allowedHeaders("*")
						.allowedOriginPatterns("*").allowCredentials(true);

			}
		};
	}

}
