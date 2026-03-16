package com.kwang.climbstyle.code.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
