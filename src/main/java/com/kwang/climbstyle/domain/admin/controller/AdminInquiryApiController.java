package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.code.inquiry.InquirySuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.inquiry.dto.request.InquiryStatusRequest;
import com.kwang.climbstyle.domain.inquiry.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminInquiryApiController {

    private final InquiryService inquiryService;

    @PatchMapping(value = "/api/v1/admin/inquiries/{inquiryNo}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> updateInquiryStatus (@PathVariable("inquiryNo") Integer inquiryNo,
                                                                           @RequestBody @Valid InquiryStatusRequest request) {

        inquiryService.updateInquiryStatus(inquiryNo, request);

        return ApiResponseBuilder.ok(InquirySuccessCode.INQUIRY_STATUS_UPDATE_SUCCESS);
    }
}
