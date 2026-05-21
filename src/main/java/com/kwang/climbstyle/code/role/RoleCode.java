package com.kwang.climbstyle.code.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 사용자 권한 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum RoleCode {
    ROLE_TEMP_USER("ROLE_TEMP_USER", "임시 사용자"),
    ROLE_USER("ROLE_USER", "일반 사용자"),
    ROLE_ADMIN("ROLE_ADMIN", "관리자"),
    ;

    private final String code;

    private final String description;
}
