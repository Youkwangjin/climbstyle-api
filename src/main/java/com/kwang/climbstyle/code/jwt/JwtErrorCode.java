package com.kwang.climbstyle.code.jwt;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum JwtErrorCode implements ApiCode {

    JWT_TOKEN_MISSING(HttpStatus.UNAUTHORIZED,             "3000", "JWT 토큰이 존재하지 않습니다."),
    JWT_TOKEN_INVALID(HttpStatus.UNAUTHORIZED,             "3001", "유효하지 않은 JWT 토큰입니다."),
    JWT_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,             "3002", "JWT 토큰이 만료되었습니다."),
    REFRESH_TOKEN_MISSING(HttpStatus.BAD_REQUEST,          "3003", "리프레시 토큰이 요청에 포함되어 있지 않습니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED,         "3004", "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,         "3005", "리프레시 토큰이 만료되었습니다."),
    COOKIE_NOT_FOUND(HttpStatus.BAD_REQUEST,               "3006", "쿠키가 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;

    JwtErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
