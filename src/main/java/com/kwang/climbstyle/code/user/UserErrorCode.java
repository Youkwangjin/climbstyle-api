package com.kwang.climbstyle.code.user;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ApiCode {
    USER_ID_INVALID_FORMAT(HttpStatus.BAD_REQUEST,          "4001", "잘못된 아이디 형식입니다."),
    USER_PASSWORD_INVALID_FORMAT(HttpStatus.BAD_REQUEST,    "4002", "잘못된 비밀번호 형식입니다."),
    USER_NAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST,        "4003", "잘못된 이름 형식입니다."),
    USER_EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST,       "4004", "잘못된 이메일 형식입니다."),
    USER_NICKNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST,    "4005", "잘못된 닉네임 형식입니다."),
    USER_PROFILE_INVALID_FORMAT(HttpStatus.BAD_REQUEST,     "4006", "잘못된 프로필 형식입니다."),
    USER_INTRO_INVALID_FORMAT(HttpStatus.BAD_REQUEST,       "4007", "잘못된 소개글 형식입니다."),

    USER_DEFAULT_ERROR(HttpStatus.BAD_REQUEST,              "4005", "입력값 형식이 잘못되었습니다."),
    USER_ALREADY_INACTIVE(HttpStatus.BAD_REQUEST,           "4006", "이미 비활성화 처리된 사용자 입니다."),
    USER_DORMANCY_COOLDOWN(HttpStatus.BAD_REQUEST,          "4007", "휴면 처리 후 3일이 지나야 비활성화할 수 있습니다."),
    USER_ORDER_EXISTS(HttpStatus.BAD_REQUEST,               "4008", "주문 내역이 존재하여 비활성화할 수 없습니다."),

    USER_PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED,         "4009", "현재 비밀번호가 일치하지 않습니다."),

    USER_INACTIVE_FORBIDDEN(HttpStatus.FORBIDDEN,           "4031", "비활성화된 사용자는 회원정보를 수정할 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,                    "4041", "존재하지 않는 사용자입니다."),

    USER_ID_DUPLICATED(HttpStatus.CONFLICT,                 "4091", "이미 사용 중인 아이디입니다."),
    USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT,              "4092", "이미 사용 중인 이메일입니다."),
    USER_NICKNAME_DUPLICATED(HttpStatus.CONFLICT,           "4093", "이미 사용 중인 닉네임 입니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
