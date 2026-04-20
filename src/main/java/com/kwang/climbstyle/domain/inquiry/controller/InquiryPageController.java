package com.kwang.climbstyle.domain.inquiry.controller;

import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.domain.inquiry.dto.response.InquiryDetailResponse;
import com.kwang.climbstyle.domain.inquiry.dto.response.InquiryListResponse;
import com.kwang.climbstyle.domain.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InquiryPageController {

    private final InquiryService inquiryService;

    @GetMapping(value = "/my/inquiries")
    public String inquiryList(CommonListRequest request, Model model) {
        List<InquiryListResponse> responses = inquiryService.getInquiryList(request);

        model.addAttribute("inquiryList", responses);
        model.addAttribute("request", request);

        return "inquiry/list";
    }

    @GetMapping(value = "/inquiries/new")
    public String newInquiry(){
        return "inquiry/new";
    }

    @GetMapping(value = "/my/inquiries/{inquiryNo}")
    public String inquiryDetail(@PathVariable Integer inquiryNo, CommonListRequest request, Model model) {
        InquiryDetailResponse response = inquiryService.getInquiryDetail(inquiryNo);

        model.addAttribute("inquiryDetail", response);
        model.addAttribute("request", request);

        return "inquiry/detail";
    }

    @GetMapping(value = "/my/inquiries/{inquiryNo}/edit")
    public String inquiryEdit(@PathVariable Integer inquiryNo, CommonListRequest request, Model model) {
        InquiryDetailResponse response = inquiryService.getInquiryDetail(inquiryNo);

        model.addAttribute("inquiryDetail", response);
        model.addAttribute("request", request);

        return "inquiry/edit";
    }
}
