package com.kwang.climbstyle.common.menu;

import com.kwang.climbstyle.code.menu.MenuCode;
import com.kwang.climbstyle.domain.menu.dto.response.UserMenuListResponse;
import com.kwang.climbstyle.domain.menu.service.MenuService;
import com.kwang.climbstyle.domain.user.dto.response.UserProfileResponse;
import com.kwang.climbstyle.security.admin.CustomAdminDetails;
import com.kwang.climbstyle.security.user.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice(basePackages = {
        "com.kwang.climbstyle.domain",
        "com.kwang.climbstyle.exception"
})
@RequiredArgsConstructor
public class UserMenuAdvice {

    private final MenuService menuService;

    @ModelAttribute("navMenus")
    public List<MenuCode> navMenus() {
        return MenuCode.getNavMenus();
    }

    @ModelAttribute("ctaMenus")
    public List<MenuCode> ctaMenus() {
        return MenuCode.getCtaMenus();
    }

    @ModelAttribute("userMenus")
    public List<UserMenuListResponse> userMenus(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return List.of();
        }

        if (authentication.getPrincipal() instanceof CustomAdminDetails) {
            return List.of();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return menuService.getUserMenuList(userDetails.getUserNo());
    }

    @ModelAttribute("currentUser")
    public UserProfileResponse currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            return UserProfileResponse.builder()
                    .userNickname(userDetails.getUserNickname())
                    .userImgUrl(userDetails.getUserImageUrl())
                    .build();
        }

        return null;
    }

    @ModelAttribute("currentUrl")
    public String currentUrl(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
