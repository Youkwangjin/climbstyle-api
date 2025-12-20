package com.kwang.climbstyle.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwang.climbstyle.security.filter.CustomUserJsonAuthenticationFilter;
import com.kwang.climbstyle.security.handler.CustomLoginSuccessHandler;
import com.kwang.climbstyle.security.handler.CustomLogoutHandler;
import com.kwang.climbstyle.security.handler.CustomUserLoginFailureHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final ObjectMapper objectMapper;

    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    private final CustomUserLoginFailureHandler customUserLoginFailureHandler;

    private final CustomLogoutHandler customLogoutHandler;

    public SpringSecurityConfig(ObjectMapper objectMapper,
                                CustomLoginSuccessHandler customLoginSuccessHandler,
                                CustomUserLoginFailureHandler customUserLoginFailureHandler,
                                CustomLogoutHandler customLogoutHandler) {
        this.objectMapper = objectMapper;
        this.customLoginSuccessHandler = customLoginSuccessHandler;
        this.customUserLoginFailureHandler = customUserLoginFailureHandler;
        this.customLogoutHandler = customLogoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomUserJsonAuthenticationFilter customUserJsonAuthenticationFilter)
            throws Exception {

        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/logout",
                                "/api/v1/users/id/availability",
                                "/api/v1/users/email/availability",
                                "/api/v1/users/nickname/availability",
                                "/api/v1/users",
                                "/api/v1/login")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                );

        http
                .formLogin(AbstractHttpConfigurer::disable);

        http
                .httpBasic(AbstractHttpConfigurer::disable);

        http
                .securityContext(context -> context
                        .requireExplicitSave(false)
                );

        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                );

        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/css/**",
                                                  "/img/**",
                                                  "/js/**").permitAll()

                                .requestMatchers("/",
                                                  "/error",
                                                  "/logout",
                                                  "/auth/login",
                                                  "/auth/register").permitAll()

                                .requestMatchers("/my/profile/**").hasAuthority("ROLE_USER")

                                .requestMatchers("/api/v1/users/id/availability",
                                                 "/api/v1/users/email/availability",
                                                 "/api/v1/users/nickname/availability",
                                                 "/api/v1/login").permitAll()

                                .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()

                                .requestMatchers(HttpMethod.PATCH, "/api/v1/users/password").hasAuthority("ROLE_USER")

                .anyRequest().permitAll()
                );

        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessHandler((request, response,
                                               authentication) ->
                                response.setStatus(HttpServletResponse.SC_OK))
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                );

        http.addFilterAt(customUserJsonAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomUserJsonAuthenticationFilter customUserJsonAuthenticationFilter(
            AuthenticationManager authenticationManager
    ) {
        CustomUserJsonAuthenticationFilter filter =
                new CustomUserJsonAuthenticationFilter(authenticationManager, objectMapper, customLoginSuccessHandler);

        filter.setAuthenticationFailureHandler(customUserLoginFailureHandler);
        return filter;
    }
}
