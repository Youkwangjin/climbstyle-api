package com.kwang.climbstyle.code.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
