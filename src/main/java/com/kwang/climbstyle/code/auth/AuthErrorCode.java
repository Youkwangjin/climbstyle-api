package com.kwang.climbstyle.code.auth;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 인증 에러 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ApiCode {
    LOGIN_ERROR(HttpStatus.UNAUTHORIZED,  "0401", "ID/PW 로그인에 실패하였습니다."),
    LOGIN_DISABLED(HttpStatus.FORBIDDEN,  "0402", "비활성화 상태의 계정입니다."),
    LOGIN_SUSPENDED(HttpStatus.FORBIDDEN, "0403", "정지된 계정입니다. 이메일을 확인해주세요."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
