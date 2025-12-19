package com.kwang.climbstyle.common.response;

import org.springframework.http.ResponseEntity;

public class ApiResponseBuilder {

    public static <T> ResponseEntity<ApiSuccessResponse<T>> ok(ApiCode code, T data) {
        ApiSuccessResponse<T> response = ApiSuccessResponse.of(
                code.getHttpStatus(),
                code.getCode(),
                code.getMessage(),
                data
        );

        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }

    public static <T> ResponseEntity<ApiSuccessResponse<T>> ok(ApiCode code) {
        ApiSuccessResponse<T> response = ApiSuccessResponse.of(
                code.getHttpStatus(),
                code.getCode(),
                code.getMessage(),
                null
        );

        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }

    public static <T> ApiSuccessResponse<T> body(ApiCode code, T data) {
        return ApiSuccessResponse.of(
                code.getHttpStatus(),
                code.getCode(),
                code.getMessage(),
                data
        );
    }

    public static <T> ApiSuccessResponse<T> body(ApiCode code) {
        return ApiSuccessResponse.of(
                code.getHttpStatus(),
                code.getCode(),
                code.getMessage(),
                null
        );
    }
}
