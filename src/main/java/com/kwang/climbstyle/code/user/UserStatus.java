package com.kwang.climbstyle.code.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 사용자 계정 상태 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE("ACTIVE", "활성"),
    DORMANT("DORMANT", "휴면"),
    SUSPENDED("SUSPENDED", "정지"),
    WITHDRAWN("WITHDRAWN", "탈퇴"),
    ;

    private final String code;

    private final String description;
}
