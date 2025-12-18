package com.kwang.climbstyle.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwang.climbstyle.code.auth.AuthSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String redirectUrl = "/";
        if (savedRequest != null) {
            redirectUrl = savedRequest.getRedirectUrl();
            requestCache.removeRequest(request, response);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("redirectUrl", request.getContextPath() + redirectUrl);

        ApiSuccessResponse<Map<String, Object>> responseBody =
                ApiResponseBuilder.successBody(AuthSuccessCode.LOGIN_SUCCESS, data);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
