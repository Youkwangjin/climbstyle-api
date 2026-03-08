package com.kwang.climbstyle.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwang.climbstyle.security.admin.CustomAdminDetailsService;
import com.kwang.climbstyle.security.filter.CustomAdminJsonAuthenticationFilter;
import com.kwang.climbstyle.security.filter.CustomUserJsonAuthenticationFilter;
import com.kwang.climbstyle.security.handler.CustomAuthenticationEntryPoint;
import com.kwang.climbstyle.security.handler.admin.CustomAdminLoginFailureHandler;
import com.kwang.climbstyle.security.handler.admin.CustomAdminLoginSuccessHandler;
import com.kwang.climbstyle.security.handler.user.CustomUserLoginSuccessHandler;
import com.kwang.climbstyle.security.handler.user.CustomUserLogoutHandler;
import com.kwang.climbstyle.security.handler.user.CustomUserLoginFailureHandler;
import com.kwang.climbstyle.security.user.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final ObjectMapper objectMapper;

    private final CustomUserLoginSuccessHandler customUserLoginSuccessHandler;

    private final CustomUserLoginFailureHandler customUserLoginFailureHandler;

    private final CustomAdminLoginSuccessHandler customAdminLoginSuccessHandler;

    private final CustomAdminLoginFailureHandler customAdminLoginFailureHandler;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final CustomUserLogoutHandler customUserLogoutHandler;

    public SpringSecurityConfig(ObjectMapper objectMapper,
                                CustomUserLoginSuccessHandler customUserLoginSuccessHandler,
                                CustomUserLoginFailureHandler customUserLoginFailureHandler,
                                CustomAdminLoginSuccessHandler customAdminLoginSuccessHandler,
                                CustomAdminLoginFailureHandler customAdminLoginFailureHandler,
                                CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                                CustomUserLogoutHandler customUserLogoutHandler) {
        this.objectMapper = objectMapper;
        this.customUserLoginSuccessHandler = customUserLoginSuccessHandler;
        this.customUserLoginFailureHandler = customUserLoginFailureHandler;
        this.customAdminLoginSuccessHandler = customAdminLoginSuccessHandler;
        this.customAdminLoginFailureHandler = customAdminLoginFailureHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customUserLogoutHandler = customUserLogoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomUserJsonAuthenticationFilter customUserJsonAuthenticationFilter,
                                                   CustomAdminJsonAuthenticationFilter customAdminJsonAuthenticationFilter)
            throws Exception {

        http
                //.csrf(AbstractHttpConfigurer::disable);
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/logout"
                        )
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
                                        "/auth/session-expired",
                                        "/logout",
                                        "/auth/login",
                                        "/admin/auth/login",
                                        "/auth/register",
                                        "/feed",
                                        "/rankings/realtime/*",
                                        "/rankings/weekly/*",
                                        "/rankings/monthly/*",
                                        "/notice/list/*"
                                        ).permitAll()

                        .requestMatchers("/my/profile/**",
                                         "/my/feed/**").hasAuthority("ROLE_USER")

                        .requestMatchers("/api/v1/users/id/availability",
                                         "/api/v1/users/email/availability",
                                         "/api/v1/users/nickname/availability",
                                         "/api/v1/login",
                                         "/api/v1/admin/login").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/users/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/feeds").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/feeds/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/feeds").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/feeds/*").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/feeds/*").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/feeds/*/like").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/feeds/*/comments").hasAuthority("ROLE_USER")

                        .requestMatchers("/api/v1/users/**").hasAuthority("ROLE_USER")

                        .anyRequest().permitAll()
                );

        http
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                );

        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(customUserLogoutHandler)
                        .logoutSuccessHandler((request, response,
                                               authentication) ->
                                response.setStatus(HttpServletResponse.SC_OK))
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                );

        http.addFilterAt(customUserJsonAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.addFilterAt(customAdminJsonAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Primary
    @Bean("userAuthenticationManager")
    public AuthenticationManager userAuthenticationManager(
            CustomUserDetailsService customUserDetailsService,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider userProvider = new DaoAuthenticationProvider();

        userProvider.setUserDetailsService(customUserDetailsService);
        userProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return new ProviderManager(List.of(userProvider));
    }

    @Bean("adminAuthenticationManager")
    public AuthenticationManager adminAuthenticationManager(
            CustomAdminDetailsService customAdminDetailsService,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider adminProvider = new DaoAuthenticationProvider();

        adminProvider.setUserDetailsService(customAdminDetailsService);
        adminProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return new ProviderManager(List.of(adminProvider));
    }

    @Bean
    public CustomUserJsonAuthenticationFilter customUserJsonAuthenticationFilter(
            @Qualifier("userAuthenticationManager") AuthenticationManager userAuthenticationManager) {
        CustomUserJsonAuthenticationFilter filter =
                new CustomUserJsonAuthenticationFilter(userAuthenticationManager, objectMapper, customUserLoginSuccessHandler);

        filter.setAuthenticationFailureHandler(customUserLoginFailureHandler);

        return filter;
    }

    @Bean
    public CustomAdminJsonAuthenticationFilter customAdminJsonAuthenticationFilter(
            @Qualifier("adminAuthenticationManager") AuthenticationManager adminAuthenticationManager) {
        CustomAdminJsonAuthenticationFilter filter =
                new CustomAdminJsonAuthenticationFilter(adminAuthenticationManager, objectMapper, customAdminLoginSuccessHandler);

        filter.setAuthenticationFailureHandler(customAdminLoginFailureHandler);

        return filter;
    }
}
