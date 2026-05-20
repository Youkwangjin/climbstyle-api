package com.kwang.climbstyle.code.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 이메일 인증 목적 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum VerificationPurpose {
    REGISTER("EMAIL_VERIFICATION",   "mail/verify-register", "[ClimbStyle] 이메일 인증번호 안내"),
    FIND_ID ("ID_FIND_VERIFICATION", "mail/verify-find-id",  "[ClimbStyle] 아이디 찾기 인증번호 안내"),
    FIND_PW ("PW_FIND_VERIFICATION", "mail/verify-find-pw",  "[ClimbStyle] 비밀번호 찾기 인증번호 안내"),
    ;

    private final String sessionKey;

    private final String template;

    private final String subject;
}
