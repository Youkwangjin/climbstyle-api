package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.code.faq.FaqSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.faq.dto.request.FaqCreateRequest;
import com.kwang.climbstyle.domain.faq.dto.request.FaqUpdateRequest;
import com.kwang.climbstyle.domain.faq.service.FaqService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminFaqApiController {

    private final FaqService faqService;

    @PostMapping(value = "/api/v1/faqs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createFaq(@RequestBody @Valid FaqCreateRequest request) {
        faqService.createFaq(request);

        return ApiResponseBuilder.ok(FaqSuccessCode.FAQ_CREATE_SUCCESS);
    }

    @PatchMapping (value = "/api/v1/faqs/{faqNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> updateFaq(@PathVariable("faqNo") Integer faqNo,
                                                                @RequestBody @Valid FaqUpdateRequest request) {
        faqService.updateFaq(faqNo, request);

        return ApiResponseBuilder.ok(FaqSuccessCode.FAQ_UPDATE_SUCCESS, faqNo);
    }

    @DeleteMapping (value = "/api/v1/faqs/{faqNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> deleteFaq(@PathVariable("faqNo") Integer faqNo) {
        faqService.deleteFaq(faqNo);

        return ApiResponseBuilder.ok(FaqSuccessCode.FAQ_DELETE_SUCCESS);
    }
}
