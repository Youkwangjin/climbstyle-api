package com.kwang.climbstyle.security.handler.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwang.climbstyle.code.auth.AuthSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.menu.dto.response.UserMenuListResponse;
import com.kwang.climbstyle.domain.menu.service.MenuService;
import com.kwang.climbstyle.security.user.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomUserLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    private final MenuService menuService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<UserMenuListResponse> menuList = menuService.getUserMenuList(userDetails.getUserNo());

        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String redirectUrl = "/";
        if (savedRequest != null) {
            redirectUrl = savedRequest.getRedirectUrl();
        }

        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("userMenuList", menuList);

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
