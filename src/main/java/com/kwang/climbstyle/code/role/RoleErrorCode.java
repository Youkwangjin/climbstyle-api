package com.kwang.climbstyle.code.role;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoleErrorCode implements ApiCode {
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND,          "8004", "존재하지 않는 권한입니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
