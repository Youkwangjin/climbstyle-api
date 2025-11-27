package com.kwang.climbstyle.security.filter;

import com.kwang.climbstyle.code.jwt.JwtErrorCode;
import com.kwang.climbstyle.common.util.JWTUtil;
import com.kwang.climbstyle.exception.ClimbStyleException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new ClimbStyleException(JwtErrorCode.JWT_TOKEN_INVALID);
        }

        if (authorization.length() == 7) {
            throw new ClimbStyleException(JwtErrorCode.JWT_TOKEN_MISSING);
        }

        String accessToken = authorization.substring(7);

        if (!jwtUtil.isValidToken(accessToken, true)) {
            throw new ClimbStyleException(JwtErrorCode.JWT_TOKEN_INVALID);
        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
