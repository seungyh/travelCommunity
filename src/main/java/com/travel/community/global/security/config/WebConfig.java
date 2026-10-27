package com.travel.community.global.security.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.images.base-path}")
    private String BASE_PATH;

    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    // registry.addMapping("/**")
    // .allowedOrigins("*")
    // .allowedMethods("*")
    // .allowedHeaders("*")
    // .allowCredentials(true);
    // }

    /**
     * 이미지 front로 내려줄 경로 설정
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = Paths.get(BASE_PATH, "public")
                .toUri()
                .toString();

        registry.addResourceHandler("/public/**")
                .addResourceLocations(location);
    }
}
