package com.kwang.climbstyle.security.handler.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwang.climbstyle.code.auth.AuthErrorCode;
import com.kwang.climbstyle.common.response.ApiErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomUserLoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof LockedException) {
            ApiErrorResponse body = ApiErrorResponse.builder()
                    .httpStatus(AuthErrorCode.LOGIN_SUSPENDED.getHttpStatus())
                    .code(AuthErrorCode.LOGIN_SUSPENDED.getCode())
                    .message(AuthErrorCode.LOGIN_SUSPENDED.getMessage())
                    .build();

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(objectMapper.writeValueAsString(body));

            return;
        }

        if (exception instanceof DisabledException) {
            String userId = (String) request.getAttribute("userId");
            request.getSession(true).setAttribute("reactivateUserId", userId);

            ApiErrorResponse body = ApiErrorResponse.builder()
                    .httpStatus(AuthErrorCode.LOGIN_DISABLED.getHttpStatus())
                    .code(AuthErrorCode.LOGIN_DISABLED.getCode())
                    .message(AuthErrorCode.LOGIN_DISABLED.getMessage())
                    .build();

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(objectMapper.writeValueAsString(body));

            return;
        }

        ApiErrorResponse body = ApiErrorResponse.builder()
                .httpStatus(AuthErrorCode.LOGIN_ERROR.getHttpStatus())
                .code(AuthErrorCode.LOGIN_ERROR.getCode())
                .message(AuthErrorCode.LOGIN_ERROR.getMessage())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
