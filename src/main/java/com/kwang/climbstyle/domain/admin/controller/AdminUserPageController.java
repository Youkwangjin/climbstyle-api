package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.code.user.UserSuspendCategory;
import com.kwang.climbstyle.domain.admin.dto.request.AdminUserListRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminUserListResponse;
import com.kwang.climbstyle.domain.user.dto.response.UserProfileResponse;
import com.kwang.climbstyle.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 관리자 사용자 페이지 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@Controller
@RequiredArgsConstructor
public class AdminUserPageController {

    private final UserService userService;

    /**
     * 사용자 목록 페이지
     */
    @GetMapping(value = "/admin/users")
    public String adminUserList(AdminUserListRequest request, Model model) {
        List<AdminUserListResponse> responses = userService.getAdminUserList(request);

        model.addAttribute("userList", responses);
        model.addAttribute("request", request);

        return "admin/user/list";
    }

    /**
     * 사용자 상세 페이지
     */
    @GetMapping(value = "/admin/users/{userNo}")
    public String adminUserDetail(@PathVariable("userNo") Integer userNo, AdminUserListRequest request, Model model) {
        UserProfileResponse response = userService.selectUserByNo(userNo);

        model.addAttribute("user", response);
        model.addAttribute("request", request);
        model.addAttribute("suspendCategories", UserSuspendCategory.values());

        return "admin/user/detail";
    }
}
