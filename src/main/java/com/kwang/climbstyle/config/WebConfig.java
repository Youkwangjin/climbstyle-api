package com.kwang.climbstyle.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${profiles.name.local}")
    private String LOCAL;

    @Value("${file.upload.base-path:}")
    private String baseUploadPath;

    @Value("${file.access.base-url:}")
    private String baseAccessUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (LOCAL.equals(profile)) {
            registry.addResourceHandler(baseAccessUrl + "**")
                    .addResourceLocations("file:" + baseUploadPath);
        }
    }
}