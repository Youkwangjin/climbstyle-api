package com.kwang.climbstyle.domain.auth;

import jakarta.servlet.http.HttpServletRequest;
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
}
