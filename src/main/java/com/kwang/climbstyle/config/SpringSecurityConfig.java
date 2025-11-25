package com.kwang.climbstyle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

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

        return http.build();
    }
}
