package com.kwang.climbstyle.domain.faq.controller;

import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.domain.faq.dto.response.FaqListResponse;
import com.kwang.climbstyle.domain.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FaqPageController {

    private final FaqService faqService;

    @GetMapping(value = "/faqs")
    public String faqList(CommonListRequest request, Model model) {
        List<FaqListResponse> responses = faqService.getFaqList(request);

        model.addAttribute("faqList", responses);
        model.addAttribute("request", request);

        return "/faq/list";
    }
}
