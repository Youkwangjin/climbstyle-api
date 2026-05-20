package com.kwang.climbstyle.exception.faq;

import com.kwang.climbstyle.code.faq.FaqErrorCode;
import com.kwang.climbstyle.common.response.ApiErrorResponse;
import com.kwang.climbstyle.domain.admin.controller.AdminFaqApiController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * FAQ 유효성 검사 예외 핸들러
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@RestControllerAdvice(assignableTypes = {AdminFaqApiController.class})
public class FaqValidationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (fieldErrors.isEmpty()) {
            FaqErrorCode defaultCode = FaqErrorCode.FAQ_DEFAULT_ERROR;

            ApiErrorResponse defaultResponse = ApiErrorResponse.builder()
                    .httpStatus(defaultCode.getHttpStatus())
                    .code(defaultCode.getCode())
                    .message(defaultCode.getMessage())
                    .build();

            return ResponseEntity.status(defaultCode.getHttpStatus()).body(defaultResponse);
        }

        FaqErrorCode faqErrorCode = getFaqErrorCode(fieldErrors);

        ApiErrorResponse response = ApiErrorResponse.builder()
                .httpStatus(faqErrorCode.getHttpStatus())
                .code(faqErrorCode.getCode())
                .message(faqErrorCode.getMessage())
                .build();

        return ResponseEntity.status(faqErrorCode.getHttpStatus()).body(response);
    }

    private static FaqErrorCode getFaqErrorCode(List<FieldError> fieldErrors) {
        String[] fieldPriority = {"faqQuestion", "faqAnswer", "faqVisibleYn"};

        for (String priorityField : fieldPriority) {
            for (FieldError fieldError : fieldErrors) {
                if (fieldError.getField().equals(priorityField)) {
                    switch (fieldError.getField()) {
                        case "faqQuestion":  return FaqErrorCode.FAQ_QUESTION_INVALID_FORMAT;
                        case "faqAnswer":    return FaqErrorCode.FAQ_ANSWER_INVALID_FORMAT;
                        case "faqVisibleYn": return FaqErrorCode.FAQ_VISIBLE_INVALID_FORMAT;
                    }
                }
            }
        }

        return FaqErrorCode.FAQ_DEFAULT_ERROR;
    }
}
