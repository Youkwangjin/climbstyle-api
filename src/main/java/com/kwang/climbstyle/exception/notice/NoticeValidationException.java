package com.kwang.climbstyle.exception.notice;

import com.kwang.climbstyle.code.notice.NoticeErrorCode;
import com.kwang.climbstyle.common.response.ApiErrorResponse;
import com.kwang.climbstyle.domain.notice.controller.AdminNoticeApiController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice(assignableTypes = {AdminNoticeApiController.class})
public class NoticeValidationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (fieldErrors.isEmpty()) {
            NoticeErrorCode defaultCode = NoticeErrorCode.NOTICE_DEFAULT_ERROR;

            ApiErrorResponse defaultResponse = ApiErrorResponse.builder()
                    .httpStatus(defaultCode.getHttpStatus())
                    .code(defaultCode.getCode())
                    .message(defaultCode.getMessage())
                    .build();

            return ResponseEntity.status(defaultCode.getHttpStatus()).body(defaultResponse);
        }

        NoticeErrorCode noticeErrorCode = getFeedErrorCode(fieldErrors);

        ApiErrorResponse response = ApiErrorResponse.builder()
                .httpStatus(noticeErrorCode.getHttpStatus())
                .code(noticeErrorCode.getCode())
                .message(noticeErrorCode.getMessage())
                .build();

        return ResponseEntity.status(noticeErrorCode.getHttpStatus()).body(response);
    }

    private static NoticeErrorCode getFeedErrorCode(List<FieldError> fieldErrors) {
        String[] fieldPriority = {"noticeCategory",
                                  "noticeTitle",
                                  "noticeContent",
                                  "noticePinYn"};

        for (String priorityField : fieldPriority) {
            for (FieldError fieldError : fieldErrors) {
                if (fieldError.getField().equals(priorityField)) {
                    String fieldName = fieldError.getField();
                    switch (fieldName) {
                        case "noticeCategory": return NoticeErrorCode.NOTICE_CATEGORY_INVALID_FORMAT;
                        case "noticeTitle": return NoticeErrorCode.NOTICE_TITLE_INVALID_FORMAT;
                        case "noticeContent": return NoticeErrorCode.NOTICE_CONTENT_INVALID_FORMAT;
                        case "noticePinYn": return NoticeErrorCode.NOTICE_PIN_INVALID_FORMAT;
                    }
                }
            }
        }

        return NoticeErrorCode.NOTICE_DEFAULT_ERROR;
    }
}
