package com.kwang.climbstyle.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwang.climbstyle.code.role.RoleCode;
import com.kwang.climbstyle.security.admin.CustomAdminDetailsService;
import com.kwang.climbstyle.security.filter.CustomAdminJsonAuthenticationFilter;
import com.kwang.climbstyle.security.filter.CustomUserJsonAuthenticationFilter;
import com.kwang.climbstyle.security.handler.common.CustomAuthenticationEntryPoint;
import com.kwang.climbstyle.security.handler.admin.CustomAdminLoginFailureHandler;
import com.kwang.climbstyle.security.handler.admin.CustomAdminLoginSuccessHandler;
import com.kwang.climbstyle.security.handler.oauth2.CustomOAuth2LoginFailureHandler;
import com.kwang.climbstyle.security.handler.oauth2.CustomOAuth2LoginSuccessHandler;
import com.kwang.climbstyle.security.handler.user.CustomUserLoginSuccessHandler;
import com.kwang.climbstyle.security.handler.user.CustomUserLogoutHandler;
import com.kwang.climbstyle.security.handler.user.CustomUserLoginFailureHandler;
import com.kwang.climbstyle.security.oauth2.service.CustomOAuth2UserService;
import com.kwang.climbstyle.security.provider.CustomDaoAuthenticationProvider;
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

    private final CustomOAuth2UserService customOAuth2UserService;

    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;

    private final CustomOAuth2LoginFailureHandler customOAuth2LoginFailureHandler;


    public SpringSecurityConfig(ObjectMapper objectMapper,
                                CustomUserLoginSuccessHandler customUserLoginSuccessHandler,
                                CustomUserLoginFailureHandler customUserLoginFailureHandler,
                                CustomAdminLoginSuccessHandler customAdminLoginSuccessHandler,
                                CustomAdminLoginFailureHandler customAdminLoginFailureHandler,
                                CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                                CustomUserLogoutHandler customUserLogoutHandler,
                                CustomOAuth2UserService customOAuth2UserService,
                                CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler,
                                CustomOAuth2LoginFailureHandler customOAuth2LoginFailureHandler) {
        this.objectMapper = objectMapper;
        this.customUserLoginSuccessHandler = customUserLoginSuccessHandler;
        this.customUserLoginFailureHandler = customUserLoginFailureHandler;
        this.customAdminLoginSuccessHandler = customAdminLoginSuccessHandler;
        this.customAdminLoginFailureHandler = customAdminLoginFailureHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customUserLogoutHandler = customUserLogoutHandler;
        this.customOAuth2UserService = customOAuth2UserService;
        this.customOAuth2LoginSuccessHandler = customOAuth2LoginSuccessHandler;
        this.customOAuth2LoginFailureHandler = customOAuth2LoginFailureHandler;
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
                                        "/login/oauth2/code/**",
                                        "/oauth2/authorization/**",
                                        "/feed",
                                        "/rankings/realtime/*",
                                        "/rankings/weekly/*",
                                        "/rankings/monthly/*",
                                        "/notices*"
                                        ).permitAll()

                        .requestMatchers("/my/profile/**",
                                         "/my/feed/**",
                                         "/feed/new").hasAuthority(RoleCode.ROLE_USER.getCode())

                        .requestMatchers("/api/v1/users/id/availability",
                                         "/api/v1/users/email/availability",
                                         "/api/v1/users/nickname/availability",
                                         "/api/v1/login",
                                         "/api/v1/admin/login").permitAll()

                        .requestMatchers("/auth/oauth2/profile").hasAuthority(RoleCode.ROLE_TEMP_USER.getCode())
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/oauth2").hasAuthority(RoleCode.ROLE_TEMP_USER.getCode())

                        .requestMatchers(HttpMethod.POST, "/api/v1/users/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/reactivate").permitAll()

                        .requestMatchers("/api/v1/users/**").hasAuthority(RoleCode.ROLE_USER.getCode())

                        .requestMatchers(HttpMethod.GET, "/api/v1/feeds").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/feeds/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/feeds").hasAuthority(RoleCode.ROLE_USER.getCode())
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/feeds/*").hasAuthority(RoleCode.ROLE_USER.getCode())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/feeds/*").hasAuthority(RoleCode.ROLE_USER.getCode())
                        .requestMatchers(HttpMethod.POST, "/api/v1/feeds/*/like").hasAuthority(RoleCode.ROLE_USER.getCode())
                        .requestMatchers(HttpMethod.POST, "/api/v1/feeds/*/comments").hasAuthority(RoleCode.ROLE_USER.getCode())

                        .requestMatchers("/admin/**").hasAuthority(RoleCode.ROLE_ADMIN.getCode())

                        .requestMatchers(HttpMethod.POST, "/api/v1/notices").hasAuthority(RoleCode.ROLE_ADMIN.getCode())
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/notices/*").hasAuthority(RoleCode.ROLE_ADMIN.getCode())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/notices/*").hasAuthority(RoleCode.ROLE_ADMIN.getCode())

                        .anyRequest().permitAll()
                );

        http
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                );

        http
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(customOAuth2LoginSuccessHandler)
                        .failureHandler(customOAuth2LoginFailureHandler)

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
            CustomDaoAuthenticationProvider customDaoAuthenticationProvider) {
        return new ProviderManager(List.of(customDaoAuthenticationProvider));
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
