package com.kwang.climbstyle.code.auth;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements ApiCode {
    LOGIN_SUCCESS(HttpStatus.OK, "1000", "로그인에 성공했습니다.");

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
