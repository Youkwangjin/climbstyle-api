package com.kwang.climbstyle.security.filter;

import com.kwang.climbstyle.code.jwt.JwtErrorCode;
import com.kwang.climbstyle.common.util.JWTUtil;
import com.kwang.climbstyle.domain.jwt.dto.response.JwtTokenResponse;
import com.kwang.climbstyle.domain.jwt.service.JwtService;
import com.kwang.climbstyle.exception.ClimbStyleException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private final JwtService jwtService;

    private static final String ACCESS_TOKEN_COOKIE_NAME = "ACCESS_TOKEN";

    private static final String REFRESH_TOKEN_COOKIE_NAME = "REFRESH_TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = null;
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), ACCESS_TOKEN_COOKIE_NAME)) {
                    accessToken = cookie.getValue();
                } else if (StringUtils.equals(cookie.getName(), REFRESH_TOKEN_COOKIE_NAME)) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean accessValid = jwtUtil.isValidToken(accessToken, true);

        if (!accessValid) {
            if (!(refreshToken != null && jwtUtil.isValidToken(refreshToken, false)
                                       && jwtService.existRefreshToken(refreshToken))) {
                throw new ClimbStyleException(JwtErrorCode.ACCESS_TOKEN_INVALID);
            }

            JwtTokenResponse newTokens = jwtService.refreshRotate(refreshToken);

            String newAccessToken = newTokens.getAccessToken();
            String newRefreshToken = newTokens.getRefreshToken();

            Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, newAccessToken);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(false);
            accessTokenCookie.setPath("/");
            response.addCookie(accessTokenCookie);

            Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, newRefreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(false);
            refreshTokenCookie.setPath("/");
            response.addCookie(refreshTokenCookie);

            accessToken = newAccessToken;
        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}