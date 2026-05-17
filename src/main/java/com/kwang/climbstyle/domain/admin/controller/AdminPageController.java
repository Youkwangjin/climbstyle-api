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

/**
 * 관리자 공통 페이지 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@Controller
@RequiredArgsConstructor
public class AdminPageController {

    private final AdminService adminService;

    /**
     * 관리자 로그인 페이지
     */
    @GetMapping(value = "/admin/auth/login")
    public String login(){
        return "admin/auth/login";
    }

    /**
     * 관리자 대시보드 페이지
     */
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
