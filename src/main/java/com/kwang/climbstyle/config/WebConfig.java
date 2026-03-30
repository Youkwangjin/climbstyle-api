package com.kwang.climbstyle.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("local")
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.base-path:}")
    private String baseUploadPath;

    @Value("${file.access.base-url:}")
    private String baseAccessUrl;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler(baseAccessUrl + "**")
                .addResourceLocations("file:" + baseUploadPath)
                .setCachePeriod(3600)
                .resourceChain(true);
    }
}