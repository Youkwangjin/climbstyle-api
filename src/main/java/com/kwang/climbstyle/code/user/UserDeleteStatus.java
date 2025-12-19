package com.kwang.climbstyle.code.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserDeleteStatus {
    ACTIVE("N", "활성화"),
    INACTIVE("Y", "비활성화"),
    ;

    private final String code;

    private final String description;
}
