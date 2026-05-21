package com.kwang.climbstyle.domain.inquiry.controller;

import com.kwang.climbstyle.code.inquiry.InquirySuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.inquiry.dto.request.InquiryCreateRequest;
import com.kwang.climbstyle.domain.inquiry.dto.request.InquiryUpdateRequest;
import com.kwang.climbstyle.domain.inquiry.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 문의 API 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@RestController
@RequiredArgsConstructor
public class InquiryApiController {

    private final InquiryService inquiryService;

    /**
     * 문의 등록
     */
    @PostMapping(value = "/api/v1/inquiries", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createInquiry(@Valid InquiryCreateRequest request) {
        inquiryService.createInquiry(request);

        return ApiResponseBuilder.ok(InquirySuccessCode.INQUIRY_CREATE_SUCCESS);
    }

    /**
     * 문의 수정
     */
    @PatchMapping(value = "/api/v1/inquiries/{inquiryNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> updateInquiry(@PathVariable("inquiryNo") Integer inquiryNo,
                                                                    @Valid InquiryUpdateRequest request) {
        inquiryService.updateInquiry(inquiryNo, request);

        return ApiResponseBuilder.ok(InquirySuccessCode.INQUIRY_UPDATE_SUCCESS);
    }

    /**
     * 문의 삭제
     */
    @DeleteMapping(value = "/api/v1/inquiries/{inquiryNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> deleteInquiry(@PathVariable("inquiryNo") Integer inquiryNo) {
        inquiryService.deleteInquiry(inquiryNo);

        return ApiResponseBuilder.ok(InquirySuccessCode.INQUIRY_DELETE_SUCCESS);
    }
}
