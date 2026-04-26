package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminFaqListResponse;
import com.kwang.climbstyle.domain.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminFaqPageController {

    private final FaqService faqService;

    @GetMapping(value = "/admin/faqs")
    public String adminFaqList(CommonListRequest request, Model model){
        List<AdminFaqListResponse> responses = faqService.getAdminFaqList(request);

        model.addAttribute("faqList", responses);
        model.addAttribute("request", request);

        return "admin/faq/list";
    }

    @GetMapping(value = "/admin/faqs/new")
    public String adminFaqNew() {
        return "admin/faq/new";
    }
}
