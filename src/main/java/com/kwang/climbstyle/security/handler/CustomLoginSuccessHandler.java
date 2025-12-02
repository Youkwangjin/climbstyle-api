package com.kwang.climbstyle.security.handler;

import com.kwang.climbstyle.common.util.JWTUtil;
import com.kwang.climbstyle.domain.jwt.service.JwtService;
import com.kwang.climbstyle.security.user.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseCookie;
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

    private static final long ACCESS_TOKEN_EXPIRE_SECONDS = 60L * 30;

    private static final long REFRESH_TOKEN_EXPIRE_SECONDS = 60L * 60 * 24 * 7;

    private static final String ACCESS_TOKEN_COOKIE_NAME = "ACCESS_TOKEN";

    private static final String REFRESH_TOKEN_COOKIE_NAME = "REFRESH_TOKEN";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Integer userNo = principal.getUserNo();
        String userId = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String jwtAccessToken = jwtUtil.createJWT(userNo, userId, role, true);
        String jwtRefreshToken = jwtUtil.createJWT(userNo, userId, role, false);

        jwtService.rotateToken(userNo, jwtRefreshToken);

        ResponseCookie accessTokenCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, jwtAccessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(ACCESS_TOKEN_EXPIRE_SECONDS)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, jwtRefreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(REFRESH_TOKEN_EXPIRE_SECONDS)
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = "{\"message\":\"LOGIN_SUCCESS\"}";
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
