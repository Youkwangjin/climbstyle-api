package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.domain.admin.dto.request.AdminInquiryListRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminInquiryListResponse;
import com.kwang.climbstyle.domain.inquiry.dto.response.InquiryDetailResponse;
import com.kwang.climbstyle.domain.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 관리자 1:1 문의 페이지 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@Controller
@RequiredArgsConstructor
public class AdminInquiryPageController {

    private final InquiryService inquiryService;

    /**
     * 문의 목록 페이지
     */
    @GetMapping(value = "/admin/inquiries")
    public String adminInquiriesList(AdminInquiryListRequest request, Model model) {
        List<AdminInquiryListResponse> responses = inquiryService.getAdminInquiryList(request);

        model.addAttribute("inquiryList", responses);
        model.addAttribute("request", request);

        return "admin/inquiry/list";
    }

    /**
     * 문의 상세 페이지
     */
    @GetMapping(value = "/admin/inquiries/{inquiryNo}")
    public String adminInquiryDetail(@PathVariable("inquiryNo") Integer inquiryNo,
                                     AdminInquiryListRequest request, Model model) {
        InquiryDetailResponse response = inquiryService.getAdminInquiryDetail(inquiryNo);

        model.addAttribute("inquiryDetail", response);
        model.addAttribute("request", request);

        return "admin/inquiry/detail";
    }
}
