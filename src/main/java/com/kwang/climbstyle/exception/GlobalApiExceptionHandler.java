package com.kwang.climbstyle.exception;

import com.kwang.climbstyle.common.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalApiExceptionHandler {

    @ExceptionHandler(ClimbStyleException.class)
    public ResponseEntity<ApiErrorResponse> handleClimbStyleException(ClimbStyleException e) {
        log.error("ClimbStyleException: {}", e.getMessage(), e);

        ApiErrorResponse response = ApiErrorResponse.builder()
                .httpStatus(e.getHttpStatus())
                .code(e.getCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }
}
