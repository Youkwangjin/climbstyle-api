package com.kwang.climbstyle.code.menu;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MenuErrorCode implements ApiCode {

    MENU_CODE_DUPLICATE(HttpStatus.CONFLICT, "10001", "이미 등록된 메뉴 코드입니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
