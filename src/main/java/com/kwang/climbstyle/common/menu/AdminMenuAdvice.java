package com.kwang.climbstyle.common.menu;

import com.kwang.climbstyle.domain.menu.dto.response.AdminMenuListResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;

/**
 * 관리자 메뉴 모델 자동 주입 어드바이스
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@ControllerAdvice(basePackages = {
        "com.kwang.climbstyle.domain.admin.controller",
        "com.kwang.climbstyle.exception"
})
@RequiredArgsConstructor
public class AdminMenuAdvice {

    @ModelAttribute("menuList")
    public List<AdminMenuListResponse> menuList(HttpSession session) {
        List<AdminMenuListResponse> menuList = (List<AdminMenuListResponse>) session.getAttribute("menuList");

        if (menuList == null) {
            return Collections.emptyList();
        }

        return menuList;
    }

    @ModelAttribute("currentUrl")
    public String currentUrl(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
