package com.kwang.climbstyle.domain.inquiry.controller;

import com.kwang.climbstyle.code.inquiry.InquirySuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.inquiry.dto.request.InquiryCreateRequest;
import com.kwang.climbstyle.domain.inquiry.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InquiryApiController {

    private final InquiryService inquiryService;

    @PostMapping(value = "/api/v1/inquiries", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createInquiry(@Valid InquiryCreateRequest request) {
        inquiryService.createInquiry(request);

        return ApiResponseBuilder.ok(InquirySuccessCode.INQUIRY_CREATE_SUCCESS);
    }
}
