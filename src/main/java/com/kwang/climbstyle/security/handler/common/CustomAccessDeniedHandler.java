package com.kwang.climbstyle.security.handler.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.common.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 접근 거부 요청 핸들러
 *
 * @author : Youkwangjin
 * @since : 2026-06-04
 * @version : 1.0
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        String uri = request.getRequestURI();

        if (!uri.startsWith("/api/")) {
            response.sendRedirect("/auth/login");
            return;
        }

        HttpErrorCode errorCode = HttpErrorCode.UNAUTHORIZED_ERROR;

        ApiErrorResponse body = ApiErrorResponse.builder()
                .httpStatus(errorCode.getHttpStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
