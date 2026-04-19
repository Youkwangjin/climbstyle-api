package com.kwang.climbstyle.code.inquiry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryDeleteCode {

    DELETED("Y", "삭제"),
    NOT_DELETED("N", "미삭제"),
    ;

    private final String code;

    private final String description;
}
