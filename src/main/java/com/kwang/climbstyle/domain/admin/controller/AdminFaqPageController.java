package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminFaqListResponse;
import com.kwang.climbstyle.domain.faq.dto.response.FaqDetailResponse;
import com.kwang.climbstyle.domain.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 관리자 FAQ 페이지 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@Controller
@RequiredArgsConstructor
public class AdminFaqPageController {

    private final FaqService faqService;

    /**
     * FAQ 목록 페이지
     */
    @GetMapping(value = "/admin/faqs")
    public String adminFaqList(CommonListRequest request, Model model){
        List<AdminFaqListResponse> responses = faqService.getAdminFaqList(request);

        model.addAttribute("faqList", responses);
        model.addAttribute("request", request);

        return "admin/faq/list";
    }

    /**
     * FAQ 상세 페이지
     */
    @GetMapping(value = "/admin/faqs/{faqNo}")
    public String adminFaqDetail(@PathVariable("faqNo") Integer faqNo, CommonListRequest request, Model model) {
        FaqDetailResponse response = faqService.getAdminFaqDetail(faqNo);

        model.addAttribute("faqDetail", response);
        model.addAttribute("request", request);

        return "admin/faq/detail";
    }

    /**
     * FAQ 등록 페이지
     */
    @GetMapping(value = "/admin/faqs/new")
    public String adminFaqNew() {
        return "admin/faq/new";
    }

    /**
     * FAQ 수정 페이지
     */
    @GetMapping(value = "/admin/faqs/{faqNo}/edit")
    public String adminFaqEdit(@PathVariable("faqNo") Integer faqNo, CommonListRequest request, Model model) {
        FaqDetailResponse response = faqService.getAdminFaqDetail(faqNo);

        model.addAttribute("faqDetail", response);
        model.addAttribute("request", request);

        return "admin/faq/edit";
    }
}
