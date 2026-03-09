package com.kwang.climbstyle.domain.auth;

import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.security.user.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {

    @GetMapping(value = "/auth/register")
    public String register() {
        return "auth/register";
    }

    @GetMapping(value = "/auth/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping(value = "/auth/session-expired")
    public String sessionExpired(HttpServletRequest request) {
        boolean sessionExpired = request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid();
        if (!sessionExpired) {
            return "redirect:/";
        }
        return "auth/session-expired";
    }

    @GetMapping(value = "/auth/reactivate")
    public String reactivate() {
        CustomUserDetails userDetails = SecurityUtil.getCurrentUserDetails();
        if (userDetails == null) {
            return "redirect:/auth/login";
        }

        if (!userDetails.isInactive()) {
            return "redirect:/";
        }

        return "auth/reactivate";
    }
}
