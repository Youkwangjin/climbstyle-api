package com.kwang.climbstyle.common.util;

import com.kwang.climbstyle.security.admin.CustomAdminDetails;
import com.kwang.climbstyle.security.user.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Security Context 사용자 정보 유틸리티
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Slf4j
public class SecurityUtil {

    public static Integer getCurrentUserNo() {
        Object principal = getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getUserNo();
        }

        if (principal instanceof OAuth2User oAuth2User) {
            return oAuth2User.getAttribute("userNo");
        }

        return null;
    }

    public static Integer getCurrentAdminNo() {
        Object principal = getPrincipal();

        if (principal instanceof CustomAdminDetails adminDetails) {
            return adminDetails.adminNo();
        }

        return null;
    }

    public static CustomUserDetails getCurrentUserDetails() {
        Object principal = getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal);
        }

        return null;
    }

    public static OAuth2User getCurrentOAuth2User() {
        Object principal = getPrincipal();
        if (principal instanceof OAuth2User oAuth2User) {
            return oAuth2User;
        }

        return null;
    }

    private static Object getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            try {
                return authentication.getPrincipal();
            } catch (Exception e) {
                log.error(" =============== PrincipalData Not Found : {} =============== ", e.getMessage());
            }
        }

        return null;
    }
}
