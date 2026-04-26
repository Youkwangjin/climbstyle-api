package com.kwang.climbstyle.exception.inquiry;

import com.kwang.climbstyle.code.inquiry.InquiryErrorCode;
import com.kwang.climbstyle.common.response.ApiErrorResponse;
import com.kwang.climbstyle.domain.admin.controller.AdminInquiryApiController;
import com.kwang.climbstyle.domain.inquiry.controller.InquiryApiController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice(assignableTypes = {InquiryApiController.class, AdminInquiryApiController.class})
public class InquiryValidationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (fieldErrors.isEmpty()) {
            InquiryErrorCode defaultCode = InquiryErrorCode.INQUIRY_DEFAULT_ERROR;

            ApiErrorResponse defaultResponse = ApiErrorResponse.builder()
                    .httpStatus(defaultCode.getHttpStatus())
                    .code(defaultCode.getCode())
                    .message(defaultCode.getMessage())
                    .build();

            return ResponseEntity.status(defaultCode.getHttpStatus()).body(defaultResponse);
        }

        InquiryErrorCode inquiryErrorCode = getInquiryErrorCode(fieldErrors);

        ApiErrorResponse response = ApiErrorResponse.builder()
                .httpStatus(inquiryErrorCode.getHttpStatus())
                .code(inquiryErrorCode.getCode())
                .message(inquiryErrorCode.getMessage())
                .build();

        return ResponseEntity.status(inquiryErrorCode.getHttpStatus()).body(response);
    }

    private static InquiryErrorCode getInquiryErrorCode(List<FieldError> fieldErrors) {
        String[] fieldPriority = {"inquiryTitle", "inquiryContent", "inquiryStatus", "inquiryAnswerContent"};

        for (String priorityField : fieldPriority) {
            for (FieldError fieldError : fieldErrors) {
                if (fieldError.getField().equals(priorityField)) {
                    switch (fieldError.getField()) {
                        case "inquiryTitle": return InquiryErrorCode.INQUIRY_TITLE_INVALID_FORMAT;
                        case "inquiryContent": return InquiryErrorCode.INQUIRY_CONTENT_INVALID_FORMAT;
                        case "inquiryStatus": return InquiryErrorCode.INQUIRY_STATUS_INVALID;
                        case "inquiryAnswerContent": return InquiryErrorCode.INQUIRY_ANSWER_INVALID_FORMAT;
                    }
                }
            }
        }

        return InquiryErrorCode.INQUIRY_DEFAULT_ERROR;
    }
}
