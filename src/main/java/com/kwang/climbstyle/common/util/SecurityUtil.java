package com.kwang.climbstyle.common.util;

import com.kwang.climbstyle.security.user.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {
    public static Integer getCurrentUserNo() {
        Object principal = getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUserNo();
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
