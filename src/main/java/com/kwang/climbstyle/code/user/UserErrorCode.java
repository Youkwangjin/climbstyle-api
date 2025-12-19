package com.kwang.climbstyle.code.user;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ApiCode {
    USER_ID_INVALID_FORMAT(HttpStatus.BAD_REQUEST,          "4000", "잘못된 아이디 형식입니다."),
    USER_PASSWORD_INVALID_FORMAT(HttpStatus.BAD_REQUEST,    "4000", "잘못된 비밀번호 형식입니다."),
    USER_EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST,       "4000", "잘못된 이메일 형식입니다."),
    USER_NICKNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST,    "4000", "잘못된 닉네임 형식입니다."),

    USER_DEFAULT_ERROR(HttpStatus.BAD_REQUEST,              "4000", "입력값 형식이 잘못되었습니다."),

    USER_PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED,         "4001", "현재 비밀번호가 일치하지 않습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,                    "4004", "존재하지 않는 사용자입니다."),

    USER_ID_DUPLICATED(HttpStatus.CONFLICT,                 "4009", "이미 사용 중인 아이디입니다."),
    USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT,              "4009", "이미 사용 중인 이메일입니다."),
    USER_NICKNAME_DUPLICATED(HttpStatus.CONFLICT,           "4009", "이미 사용 중인 닉네임 입니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
