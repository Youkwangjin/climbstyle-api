package com.kwang.climbstyle.security.handler.oauth2;

import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.exception.ClimbStyleException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        if (exception.getCause() instanceof ClimbStyleException climbStyleException) {
            final String code = climbStyleException.getCode();

            if (StringUtils.equals(code, UserErrorCode.USER_ALREADY_DORMANT.getCode())) {
                response.sendRedirect("/auth/login?error=dormant");
                return;
            }

            if (StringUtils.equals(code, UserErrorCode.USER_ALREADY_SUSPENDED.getCode())) {
                response.sendRedirect("/auth/login?error=suspended");
                return;
            }

            if (StringUtils.equals(code, UserErrorCode.USER_EMAIL_DUPLICATED.getCode())) {
                response.sendRedirect("/auth/login?error=email_duplicated");
                return;
            }
        }

        response.sendRedirect("/auth/login?error=oauth2");
    }
}
