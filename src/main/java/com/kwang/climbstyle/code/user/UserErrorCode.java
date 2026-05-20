package com.kwang.climbstyle.code.user;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 사용자 에러 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ApiCode {
    USER_ID_INVALID_FORMAT(HttpStatus.BAD_REQUEST,                "4001", "잘못된 아이디 형식입니다."),
    USER_PASSWORD_INVALID_FORMAT(HttpStatus.BAD_REQUEST,          "4002", "잘못된 비밀번호 형식입니다."),
    USER_NAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST,              "4003", "잘못된 이름 형식입니다."),
    USER_EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST,             "4004", "잘못된 이메일 형식입니다."),
    USER_NICKNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST,          "4005", "잘못된 닉네임 형식입니다."),
    USER_PROFILE_INVALID_FORMAT(HttpStatus.BAD_REQUEST,           "4006", "잘못된 프로필 형식입니다."),
    USER_INTRO_INVALID_FORMAT(HttpStatus.BAD_REQUEST,             "4007", "잘못된 소개글 형식입니다."),

    USER_DEFAULT_ERROR(HttpStatus.BAD_REQUEST,                    "4005", "입력값 형식이 잘못되었습니다."),
    USER_ALREADY_DORMANT(HttpStatus.BAD_REQUEST,                  "4006", "이미 비활성화 처리된 사용자 입니다."),
    USER_ALREADY_REACTIVATE(HttpStatus.BAD_REQUEST,               "4007", "이미 활성화 처리된 사용자 입니다."),
    USER_ALREADY_SUSPENDED(HttpStatus.BAD_REQUEST,                "4008", "이미 정지 처리된 사용자 입니다."),
    USER_NOT_SUSPENDED(HttpStatus.BAD_REQUEST,                    "4009", "정지 처리된 사용자가 아닙니다."),
    USER_DORMANCY_COOLDOWN(HttpStatus.BAD_REQUEST,                "4010", "휴면 처리 후 3일이 지나야 비활성화할 수 있습니다."),
    USER_ORDER_EXISTS(HttpStatus.BAD_REQUEST,                     "4011", "주문 내역이 존재하여 비활성화할 수 없습니다."),
    USER_OAUTH_NO_PASSWORD(HttpStatus.BAD_REQUEST,                "4012", "소셜 로그인 사용자는 비밀번호를 변경할 수 없습니다."),

    USER_PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED,               "4013", "현재 비밀번호가 일치하지 않습니다."),
    USER_EMAIL_VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "4014", "인증번호가 올바르지 않습니다."),
    USER_EMAIL_VERIFICATION_EXPIRED(HttpStatus.BAD_REQUEST,       "4015", "인증번호가 만료되었습니다. 다시 발송해주세요."),
    USER_EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST,               "4016", "이메일 인증을 완료해주세요."),

    USER_ALREADY_WITHDRAWN(HttpStatus.BAD_REQUEST,                "4017", "이미 탈퇴 처리된 사용자입니다."),

    USER_DORMANT_FORBIDDEN(HttpStatus.FORBIDDEN,                  "4018", "비활성화된 사용자는 회원정보를 수정할 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,                          "4019", "존재하지 않는 사용자입니다."),
    USER_FIND_ID_NOT_FOUND(HttpStatus.NOT_FOUND,                  "4020", "입력하신 정보와 일치하는 아이디가 없습니다."),
    USER_FIND_PW_NOT_FOUND(HttpStatus.NOT_FOUND,                  "4021", "입력하신 정보와 일치하는 계정이 없습니다."),

    USER_ID_DUPLICATED(HttpStatus.CONFLICT,                       "4022", "이미 사용 중인 아이디입니다."),
    USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT,                    "4023", "이미 사용 중인 이메일입니다."),
    USER_NICKNAME_DUPLICATED(HttpStatus.CONFLICT,                 "4024", "이미 사용 중인 닉네임 입니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
