package com.kwang.climbstyle.code.http;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HttpErrorCode implements ApiCode {
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST,               "9400", "잘못된 요청입니다."),
    UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED,             "9401", "로그인 후 이용 가능합니다."),
    FORBIDDEN_ERROR(HttpStatus.FORBIDDEN,                   "9403", "접근 권한이 없습니다."),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND,                   "9404", "요청하신 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED_ERROR(HttpStatus.METHOD_NOT_ALLOWED, "9405", "허용되지 않은 요청 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "9999", "서버 내부 오류가 발생하였습니다. 관리자에게 문의하세요.");

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
