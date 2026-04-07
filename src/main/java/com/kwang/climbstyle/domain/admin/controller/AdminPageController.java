package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.domain.admin.dto.response.AdminDashboardStatResponse;
import com.kwang.climbstyle.domain.admin.dto.response.AdminInquiryListResponse;
import com.kwang.climbstyle.domain.admin.dto.response.AdminNoticeListResponse;
import com.kwang.climbstyle.domain.admin.dto.response.AdminUserListResponse;
import com.kwang.climbstyle.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminPageController {

    private final AdminService adminService;

    @GetMapping(value = "/admin/auth/login")
    public String login(){
        return "admin/auth/login";
    }

    @GetMapping(value = "/admin/index")
    public String mypage(Model model) {
        AdminDashboardStatResponse stat = adminService.getDashboardStat();
        List<AdminUserListResponse> userList = adminService.getUserList();
        List<AdminInquiryListResponse> inquiryList = adminService.getInquiryList();
        List<AdminNoticeListResponse> noticeList = adminService.getNoticeList();

        model.addAttribute("stat", stat);
        model.addAttribute("recentUsers", userList);
        model.addAttribute("inquiryList", inquiryList);
        model.addAttribute("noticeList", noticeList);

        return "admin/index";
    }
}
