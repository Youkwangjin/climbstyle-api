package com.kwang.climbstyle.domain.faq.controller;

import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.domain.faq.dto.response.FaqListResponse;
import com.kwang.climbstyle.domain.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * FAQ 페이지 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Controller
@RequiredArgsConstructor
public class FaqPageController {

    private final FaqService faqService;

    /**
     * FAQ 목록 화면
     * [ClimbStyle] > [FAQ]
     */
    @GetMapping(value = "/faqs")
    public String faqList(CommonListRequest request, Model model) {
        List<FaqListResponse> responses = faqService.getFaqList(request);

        model.addAttribute("faqList", responses);
        model.addAttribute("request", request);

        return "faq/list";
    }
}
