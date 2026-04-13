package com.kwang.climbstyle.domain.notice.controller;

import com.kwang.climbstyle.code.notice.NoticeSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.notice.dto.request.NoticeCreateRequest;
import com.kwang.climbstyle.domain.notice.dto.request.NoticeUpdateRequest;
import com.kwang.climbstyle.domain.notice.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminNoticeApiController {

    private final NoticeService noticeService;

    @PostMapping(value = "/api/v1/notices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createNotice(@Valid NoticeCreateRequest request) {
        noticeService.createNotice(request);

        return ApiResponseBuilder.ok(NoticeSuccessCode.NOTICE_CREATE_SUCCESS);
    }

    @PatchMapping(value = "/api/v1/notices/{noticeNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> updateNotice(@Valid NoticeUpdateRequest request,
                                                                   @PathVariable("noticeNo") Integer noticeNo) {
        noticeService.updateNotice(request, noticeNo);

        return ApiResponseBuilder.ok(NoticeSuccessCode.NOTICE_UPDATE_SUCCESS);
    }
}
