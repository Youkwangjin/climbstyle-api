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

/**
 * 문의 페이지 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Controller
@RequiredArgsConstructor
public class InquiryPageController {

    private final InquiryService inquiryService;

    /**
     * 문의 목록 화면
     * [ClimbStyle] > [마이페이지] > 문의 내역
     */
    @GetMapping(value = "/my/inquiries")
    public String inquiryList(CommonListRequest request, Model model) {
        List<InquiryListResponse> responses = inquiryService.getInquiryList(request);

        model.addAttribute("inquiryList", responses);
        model.addAttribute("request", request);

        return "inquiry/list";
    }

    /**
     * 문의 작성 화면
     * [ClimbStyle] > [마이페이지] > 문의 내역 > 문의 작성
     */
    @GetMapping(value = "/my/inquiries/new")
    public String newInquiry(){
        return "inquiry/new";
    }

    /**
     * 문의 상세 화면
     * [ClimbStyle] > [마이페이지] > 문의 내역 > 상세
     */
    @GetMapping(value = "/my/inquiries/{inquiryNo}")
    public String inquiryDetail(@PathVariable Integer inquiryNo, CommonListRequest request, Model model) {
        InquiryDetailResponse response = inquiryService.getInquiryDetail(inquiryNo);

        model.addAttribute("inquiryDetail", response);
        model.addAttribute("request", request);

        return "inquiry/detail";
    }

    /**
     * 문의 수정 화면
     * [ClimbStyle] > [마이페이지] > 문의 내역 > 수정
     */
    @GetMapping(value = "/my/inquiries/{inquiryNo}/edit")
    public String inquiryEdit(@PathVariable Integer inquiryNo, CommonListRequest request, Model model) {
        InquiryDetailResponse response = inquiryService.getInquiryDetail(inquiryNo);

        model.addAttribute("inquiryDetail", response);
        model.addAttribute("request", request);

        return "inquiry/edit";
    }
}
