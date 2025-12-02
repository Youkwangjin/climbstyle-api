package com.kwang.climbstyle.code.jwt;

import lombok.Getter;

@Getter
public enum JwtRefreshStatus {
    ACTIVE("N", "사용 가능"),
    REVOKED("Y", "사용 불가");

    private final String code;

    private final String description;

    JwtRefreshStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
