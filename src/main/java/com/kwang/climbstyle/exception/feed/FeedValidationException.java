package com.kwang.climbstyle.exception.feed;

import com.kwang.climbstyle.code.feed.FeedErrorCode;
import com.kwang.climbstyle.common.response.ApiErrorResponse;
import com.kwang.climbstyle.domain.feed.controller.FeedApiController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice(assignableTypes = {FeedApiController.class})
public class FeedValidationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (fieldErrors.isEmpty()) {
            FeedErrorCode defaultCode = FeedErrorCode.FEED_DEFAULT_ERROR;

            ApiErrorResponse defaultResponse = ApiErrorResponse.builder()
                    .httpStatus(defaultCode.getHttpStatus())
                    .code(defaultCode.getCode())
                    .message(defaultCode.getMessage())
                    .build();

            return ResponseEntity.status(defaultCode.getHttpStatus()).body(defaultResponse);
        }

        FeedErrorCode feedErrorCode  = getFeedErrorCode(fieldErrors);

        ApiErrorResponse response = ApiErrorResponse.builder()
                .httpStatus(feedErrorCode.getHttpStatus())
                .code(feedErrorCode.getCode())
                .message(feedErrorCode.getMessage())
                .build();

        return ResponseEntity.status(feedErrorCode.getHttpStatus()).body(response);
    }

    private static FeedErrorCode getFeedErrorCode(List<FieldError> fieldErrors) {
        String[] fieldPriority = {"feedTitle",
                                  "feedContent",
                                  "feedVisibleYn",
                                  "feedImages",
                                  "feedCommentContent"};

        for (String priorityField : fieldPriority) {
            for (FieldError fieldError : fieldErrors) {
                if (fieldError.getField().equals(priorityField)) {
                    String fieldName = fieldError.getField();
                    switch (fieldName) {
                        case "feedTitle": return FeedErrorCode.FEED_TITLE_INVALID_FORMAT;
                        case "feedContent": return FeedErrorCode.FEED_CONTENT_INVALID_FORMAT;
                        case "feedVisibleYn": return FeedErrorCode.FEED_VISIBLE_INVALID_FORMAT;
                        case "feedImages": return FeedErrorCode.FEED_IMAGE_REQUIRED;
                        case "feedCommentContent": return FeedErrorCode.FEED_COMMENT_INVALID_FORMAT;
                    }
                }
            }
        }

        return FeedErrorCode.FEED_DEFAULT_ERROR;
    }
}
