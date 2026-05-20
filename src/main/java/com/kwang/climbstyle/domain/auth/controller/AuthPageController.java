package com.kwang.climbstyle.domain.auth.controller;

import com.kwang.climbstyle.code.user.VerificationPurpose;
import com.kwang.climbstyle.domain.user.dto.EmailVerificationData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 인증 페이지 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Controller
public class AuthPageController {

    /**
     * 회원가입 화면
     * [ClimbStyle] > [회원가입]
     */
    @GetMapping(value = "/auth/register")
    public String register() {
        return "auth/register";
    }

    /**
     * 로그인 화면
     * [ClimbStyle] > [로그인]
     */
    @GetMapping(value = "/auth/login")
    public String login() {
        return "auth/login";
    }

    /**
     * 아이디 찾기 화면
     * [ClimbStyle] > [아이디 찾기]
     */
    @GetMapping("/auth/forgot/id")
    public String forgotId() {
        return "auth/forgot-id";
    }

    /**
     * 비밀번호 찾기 화면
     * [ClimbStyle] > [비밀번호 찾기]
     */
    @GetMapping("/auth/forgot/password")
    public String forgotPassword() {
        return "auth/forgot-password";
    }

    /**
     * 비밀번호 재설정 화면
     * [ClimbStyle] > [비밀번호 찾기] > 비밀번호 재설정
     */
    @GetMapping("/auth/forgot/password/reset")
    public String resetPassword(HttpSession session, Model model) {
        EmailVerificationData data = (EmailVerificationData) session.getAttribute(VerificationPurpose.FIND_PW.getSessionKey());
        if (data == null || !data.isVerified()) {
            return "redirect:/auth/forgot/password";
        }

        final String userEmail = data.getEmail();
        model.addAttribute("userEmail", userEmail);

        return "auth/reset-password";
    }

    /**
     * 세션 만료 화면
     */
    @GetMapping(value = "/auth/session-expired")
    public String sessionExpired(HttpServletRequest request) {
        boolean sessionExpired = request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid();
        if (!sessionExpired) {
            return "redirect:/";
        }

        return "auth/session-expired";
    }

    /**
     * 휴면 계정 재활성화 화면
     * [ClimbStyle] > 휴면 계정 재활성화
     */
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

    /**
     * OAuth2 프로필 작성 화면
     * [ClimbStyle] > OAuth2 프로필 작성
     */
    @GetMapping("/auth/oauth2/profile")
    public String oauth2Profile() {
        return "auth/oauth2-profile";
    }
}
