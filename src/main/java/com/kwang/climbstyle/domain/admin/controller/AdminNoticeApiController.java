package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.code.notice.NoticeSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.common.editor.dto.request.CommonEditorImageUploadRequest;
import com.kwang.climbstyle.domain.common.editor.dto.response.CommonEditorImageUploadResponse;
import com.kwang.climbstyle.domain.notice.dto.request.NoticeCreateRequest;
import com.kwang.climbstyle.domain.notice.dto.request.NoticeUpdateRequest;
import com.kwang.climbstyle.domain.notice.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminNoticeApiController {

    private final NoticeService noticeService;

    @PostMapping(value = "/api/v1/notices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createNotice(@Valid NoticeCreateRequest request) {
        noticeService.createNotice(request);

        return ApiResponseBuilder.ok(NoticeSuccessCode.NOTICE_CREATE_SUCCESS);
    }

    @PostMapping(value = "/api/v1/notices/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> uploadNoticeImage(@Valid CommonEditorImageUploadRequest request) {
        CommonEditorImageUploadResponse imageUrl = noticeService.uploadNoticeImage(request);

        return ApiResponseBuilder.ok(NoticeSuccessCode.NOTICE_IMAGE_UPLOAD_SUCCESS, imageUrl);
    }

    @PatchMapping(value = "/api/v1/notices/{noticeNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> updateNotice(@Valid NoticeUpdateRequest request,
                                                                   @PathVariable("noticeNo") Integer noticeNo) {
        noticeService.updateNotice(request, noticeNo);

        return ApiResponseBuilder.ok(NoticeSuccessCode.NOTICE_UPDATE_SUCCESS);
    }

    @DeleteMapping(value = "/api/v1/notices/{noticeNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> deleteNotice(@PathVariable("noticeNo") Integer noticeNo) {
        noticeService.deleteNotice(noticeNo);

        return ApiResponseBuilder.ok(NoticeSuccessCode.NOTICE_DELETE_SUCCESS);
    }
}
