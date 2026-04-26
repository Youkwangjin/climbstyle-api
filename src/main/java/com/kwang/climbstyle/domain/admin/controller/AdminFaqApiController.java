package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.code.faq.FaqSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.faq.dto.request.FaqCreateRequest;
import com.kwang.climbstyle.domain.faq.service.FaqService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminFaqApiController {

    private final FaqService faqService;

    @PostMapping(value = "/api/v1/faqs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createFaq(@RequestBody @Valid FaqCreateRequest request) {
        faqService.createFaq(request);

        return ApiResponseBuilder.ok(FaqSuccessCode.FAQ_CREATE_SUCCESS);
    }
}
