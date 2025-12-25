package com.kwang.climbstyle.code.user;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserSuccessCode implements ApiCode {
    USER_ID_AVAILABLE(HttpStatus.OK, "1000", "사용 가능한 아이디입니다."),
    USER_EMAIL_AVAILABLE(HttpStatus.OK, "1000", "사용 가능한 이메일입니다."),
    USER_NICKNAME_AVAILABLE(HttpStatus.OK, "1000", "사용 가능한 닉네임입니다."),

    USER_REGISTER_SUCCESS(HttpStatus.OK, "1000", "회원가입이 정상적으로 완료되었습니다."),
    USER_UPDATE_SUCCESS(HttpStatus.OK,  "1000", "회원수정이 정상적으로 완료되었습니다."),
    USER_DELETE_SUCCESS(HttpStatus.OK, "1000", "계정이 비활성화 되었습니다."),
    USER_PASSWORD_UPDATE_SUCCESS(HttpStatus.OK, "1000", "비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
