package com.kwang.climbstyle.security.handler.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.common.response.ApiErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String uri = request.getRequestURI();
        boolean isApiRequest = uri.startsWith("/api/");
        boolean sessionExpired = request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid();

        if (!isApiRequest) {
            if (sessionExpired) {
                response.sendRedirect("/auth/session-expired");
                return;
            }

            response.sendRedirect("/auth/login");
            return;
        }

        HttpErrorCode httpErrorCode;
        if (sessionExpired) {
            httpErrorCode = HttpErrorCode.SESSION_EXPIRED;
        } else {
            httpErrorCode = HttpErrorCode.UNAUTHORIZED_ERROR;
        }

        ApiErrorResponse body = ApiErrorResponse.builder()
                .httpStatus(httpErrorCode.getHttpStatus())
                .code(httpErrorCode.getCode())
                .message(httpErrorCode.getMessage())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
