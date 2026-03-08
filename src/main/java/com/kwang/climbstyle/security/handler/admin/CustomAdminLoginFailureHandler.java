package com.kwang.climbstyle.security.handler.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwang.climbstyle.code.auth.AuthErrorCode;
import com.kwang.climbstyle.common.response.ApiErrorResponse;
import com.kwang.climbstyle.domain.admin.service.AdminHistoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomAdminLoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    private final AdminHistoryService adminHistoryService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String failId = (String) request.getAttribute("attemptedAdminId");

        adminHistoryService.saveFailure(failId, request);

        ApiErrorResponse responseBody = ApiErrorResponse.builder()
                .httpStatus(AuthErrorCode.LOGIN_ERROR.getHttpStatus())
                .code(AuthErrorCode.LOGIN_ERROR.getCode())
                .message(AuthErrorCode.LOGIN_ERROR.getMessage())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
