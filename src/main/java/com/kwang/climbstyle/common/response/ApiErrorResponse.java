package com.kwang.climbstyle.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * API 에러 응답 래퍼
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
public class ApiErrorResponse {

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;

    @Builder
    ApiErrorResponse(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
