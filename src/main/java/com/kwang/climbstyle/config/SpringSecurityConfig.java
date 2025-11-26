package com.kwang.climbstyle.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwang.climbstyle.security.filter.CustomUserJsonLoginFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    private final ObjectMapper objectMapper;

    private final AuthenticationSuccessHandler loginSuccessHandler;

    public SpringSecurityConfig(AuthenticationConfiguration authenticationConfiguration,
                                ObjectMapper objectMapper,
                                @Qualifier("CustomLoginSuccessHandler") AuthenticationSuccessHandler loginSuccessHandler) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.objectMapper = objectMapper;
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/css/**",
                                                  "/img/**",
                                                  "/js/**").permitAll()

                                .requestMatchers("/",
                                                  "/auth/login",
                                                  "/auth/register").permitAll()

                                .requestMatchers("/api/v1/users/id/availability",
                                                  "/api/v1/users/email/availability",
                                                  "/api/v1/users/nickname/availability").permitAll()


                                .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()

                .anyRequest().permitAll()
                );

        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .addFilterBefore(new CustomUserJsonLoginFilter(authenticationManager(authenticationConfiguration), objectMapper, loginSuccessHandler),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
