package com.kwang.climbstyle.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String reactivate(HttpServletRequest request, Model model) {
        String reactivateUserId = (String) request.getSession().getAttribute("reactivateUserId");

        if (reactivateUserId == null) {
            return "redirect:/";
        }

        request.getSession().removeAttribute("reactivateUserId");
        model.addAttribute("userId", reactivateUserId);

        return "auth/reactivate";
    }

    @GetMapping("/auth/oauth2/profile")
    public String oauth2Profile() {
        return "auth/oauth2-profile";
    }
}
