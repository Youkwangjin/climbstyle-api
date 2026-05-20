package com.kwang.climbstyle.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 정적 파일 리소스 핸들러 설정 (local 프로파일)
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
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