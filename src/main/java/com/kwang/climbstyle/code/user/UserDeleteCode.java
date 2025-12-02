package com.kwang.climbstyle.code.user;

import lombok.Getter;

@Getter
public enum UserDeleteCode {
    ACTIVE("N", "활성화"),
    INACTIVE("Y", "비활성화");

    private final String code;
    private final String description;

    UserDeleteCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
