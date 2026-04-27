package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.domain.admin.dto.request.AdminUserListRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminUserListResponse;
import com.kwang.climbstyle.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminUserPageController {

    private final UserService userService;

    @GetMapping("/admin/users")
    public String adminUserList(AdminUserListRequest request, Model model) {
        List<AdminUserListResponse> responses = userService.getAdminUserList(request);

        model.addAttribute("userList", responses);
        model.addAttribute("request", request);

        return "admin/user/list";
    }
}
