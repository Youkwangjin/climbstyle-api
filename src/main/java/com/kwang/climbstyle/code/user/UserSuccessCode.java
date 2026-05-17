package com.kwang.climbstyle.code.user;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserSuccessCode implements ApiCode {
    USER_ID_AVAILABLE(HttpStatus.OK,                        "1000", "사용 가능한 아이디입니다."),
    USER_EMAIL_AVAILABLE(HttpStatus.OK,                     "1000", "사용 가능한 이메일입니다."),
    USER_NICKNAME_AVAILABLE(HttpStatus.OK,                  "1000", "사용 가능한 닉네임입니다."),

    USER_EMAIL_VERIFICATION_CODE_SENT(HttpStatus.OK,        "1000", "인증번호가 발송되었습니다. 이메일을 확인해주세요."),
    USER_EMAIL_VERIFIED(HttpStatus.OK,                      "1000", "이메일 인증이 완료되었습니다."),

    USER_REGISTER_SUCCESS(HttpStatus.OK,                    "1000", "회원가입이 정상적으로 완료되었습니다."),
    USER_UPDATE_SUCCESS(HttpStatus.OK,                      "1000", "회원수정이 정상적으로 완료되었습니다. 안전을 위해 다시 로그인해주세요."),
    USER_DELETE_SUCCESS(HttpStatus.OK,                      "1001", "계정이 정상적으로 삭제되었습니다."),
    USER_DORMANT_SUCCESS(HttpStatus.OK,                     "1000", "계정이 비활성화 처리되었습니다."),
    USER_REACTIVATE_SUCCESS(HttpStatus.OK,                  "1000", "계정이 활성화되었습니다. 다시 로그인해주세요."),
    USER_PASSWORD_UPDATE_SUCCESS(HttpStatus.OK,             "1000", "비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요."),

    USER_FIND_ID_SUCCESS(HttpStatus.OK,                     "1000", "아이디를 찾았습니다."),
    USER_FIND_ID_REVEAL_SUCCESS(HttpStatus.OK,              "1000", "아이디 전체 확인이 완료되었습니다."),

    USER_FIND_PW_CODE_SENT(HttpStatus.OK,                   "1000", "인증번호가 발송되었습니다. 이메일을 확인해주세요."),
    USER_RESET_PASSWORD_SUCCESS(HttpStatus.OK,              "1000", "비밀번호가 성공적으로 재설정되었습니다. 다시 로그인해주세요."),

    USER_SUSPEND_SUCCESS(HttpStatus.OK,                     "1000", "사용자가 정지 처리되었습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
