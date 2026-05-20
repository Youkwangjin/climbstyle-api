package com.kwang.climbstyle.code.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 사용자 정지 사유 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum UserSuspendCategory {
    CONTENT_VIOLATION("CONTENT_VIOLATION", "컨텐츠 정책 위반"),
    SPAM("SPAM", "스팸/도배"),
    FRAUD("FRAUD", "사기/허위정보"),
    OTHER("OTHER", "기타"),
    ;

    private final String code;

    private final String description;
}
