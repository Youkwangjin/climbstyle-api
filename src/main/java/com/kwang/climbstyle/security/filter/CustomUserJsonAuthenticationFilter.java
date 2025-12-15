package com.kwang.climbstyle.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwang.climbstyle.domain.auth.dto.request.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomUserJsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String HTTP_METHOD_POST = "POST";
    private static final String LOGIN_URL = "/api/v1/login";
    private static final String CONTENT_TYPE = "application/json";
    private static final RequestMatcher DEFAULT_AUTH_MATCHER = PathPatternRequestMatcher.withDefaults()
            .matcher(HttpMethod.POST, LOGIN_URL);
    private final ObjectMapper objectMapper;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final SecurityContextRepository securityContextRepository;

    public CustomUserJsonAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, AuthenticationSuccessHandler authenticationSuccessHandler) {
        super(DEFAULT_AUTH_MATCHER, authenticationManager);
        this.objectMapper = objectMapper;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.securityContextRepository = new HttpSessionSecurityContextRepository();
        setSecurityContextRepository(this.securityContextRepository);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (request.getContentType() == null || !CONTENT_TYPE.equals(request.getContentType())) {
            throw new AuthenticationServiceException("Unsupported content type: " + request.getContentType());
        }

        if (!request.getMethod().equals(HTTP_METHOD_POST)) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        LoginRequest loginRequest = objectMapper.readValue(StreamUtils.copyToString(request.getInputStream(),
                StandardCharsets.UTF_8), LoginRequest.class);

        String username = loginRequest.getUserId();
        String password = loginRequest.getUserPassword();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new AuthenticationServiceException("Missing required fields");
        }

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authResult);
        SecurityContextHolder.setContext(securityContext);

        this.securityContextRepository.saveContext(securityContext, request, response);

        authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
    }
}
