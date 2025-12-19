package com.kwang.climbstyle.code.auth;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ApiCode {
    LOGIN_ERROR(HttpStatus.UNAUTHORIZED, "0401", "ID/PW 로그인에 실패하였습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
