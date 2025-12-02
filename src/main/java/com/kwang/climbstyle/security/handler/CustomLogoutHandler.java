package com.kwang.climbstyle.security.handler;

import com.kwang.climbstyle.common.util.JWTUtil;
import com.kwang.climbstyle.domain.jwt.service.JwtService;
import com.kwang.climbstyle.security.user.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JWTUtil jwtUtil;

    private final JwtService jwtService;

    private static final String ACCESS_TOKEN_COOKIE_NAME = "ACCESS_TOKEN";

    private static final String REFRESH_TOKEN_COOKIE_NAME = "REFRESH_TOKEN";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        Integer userNo = null;

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails principal) {
            userNo = principal.getUserNo();
        } else {
            String refreshToken = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }

            if (refreshToken != null && jwtUtil.isValidToken(refreshToken, false)) {
                userNo = jwtUtil.getUserNo(refreshToken);
            }
        }

        if (userNo != null) {
            jwtService.revokeToken(userNo);
        }

        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
    }
}

