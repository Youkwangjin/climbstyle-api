package com.kwang.climbstyle.code.menu;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 메뉴 성공 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-26
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum MenuSuccessCode implements ApiCode {

    MENU_CREATE_SUCCESS(HttpStatus.OK, "1000", "메뉴가 등록되었습니다."),
    MENU_UPDATE_SUCCESS(HttpStatus.OK, "1001", "메뉴가 수정되었습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
