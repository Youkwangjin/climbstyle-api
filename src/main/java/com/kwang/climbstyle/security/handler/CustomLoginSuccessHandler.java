package com.kwang.climbstyle.security.handler;

import com.kwang.climbstyle.common.util.JWTUtil;
import com.kwang.climbstyle.domain.jwt.service.JwtService;
import com.kwang.climbstyle.security.user.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Qualifier("CustomLoginSuccessHandler")
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Integer userNo = principal.getUserNo();
        String userId = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String jwtAccessToken = jwtUtil.createJWT(userId, role, true);
        String jwtRefreshToken = jwtUtil.createJWT(userId, role, false);

        jwtService.createToken(userNo, jwtRefreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = String.format("{\"access_token\":\"%s\",\"refresh_token\":\"%s\"}", jwtAccessToken, jwtRefreshToken);
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
