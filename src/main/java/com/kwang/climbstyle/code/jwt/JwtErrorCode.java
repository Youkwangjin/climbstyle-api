package com.kwang.climbstyle.code.jwt;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum JwtErrorCode implements ApiCode {
    ACCESS_TOKEN_MISSING(HttpStatus.UNAUTHORIZED,    "3000", "Access Token이 존재하지 않습니다."),
    ACCESS_TOKEN_INVALID(HttpStatus.UNAUTHORIZED,    "3001", "유효하지 않은 Access Token입니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,    "3002", "Access Token이 만료되었습니다."),

    REFRESH_TOKEN_MISSING(HttpStatus.UNAUTHORIZED,   "3010", "Refresh Token이 존재하지 않습니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED,   "3011", "유효하지 않은 Refresh Token입니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,   "3012", "Refresh Token이 만료되었습니다."),
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
