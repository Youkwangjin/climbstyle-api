package com.kwang.climbstyle.security.handler.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwang.climbstyle.code.auth.AuthSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.admin.service.AdminHistoryService;
import com.kwang.climbstyle.security.admin.CustomAdminDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAdminLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    private final AdminHistoryService adminHistoryService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomAdminDetails customAdminDetails = (CustomAdminDetails) authentication.getPrincipal();
        adminHistoryService.saveSuccess(customAdminDetails.adminNo(), request);

        final String redirectUrl = "/admin/mypage";

        Map<String, Object> data = new HashMap<>();
        data.put("redirectUrl", request.getContextPath() + redirectUrl);

        ApiSuccessResponse<Map<String, Object>> responseBody =
                ApiResponseBuilder.body(AuthSuccessCode.LOGIN_SUCCESS, data);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
