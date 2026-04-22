package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.domain.admin.dto.request.AdminInquiryListRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminInquiryListResponse;
import com.kwang.climbstyle.domain.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminInquiryPageController {

    private final InquiryService inquiryService;

    @GetMapping(value = "/admin/inquiries")
    public String adminInquiriesList(AdminInquiryListRequest request, Model model) {
        List<AdminInquiryListResponse> responses = inquiryService.getAdminInquiryList(request);

        model.addAttribute("inquiryList", responses);
        model.addAttribute("request", request);

        return "admin/inquiry/list";
    }
}
