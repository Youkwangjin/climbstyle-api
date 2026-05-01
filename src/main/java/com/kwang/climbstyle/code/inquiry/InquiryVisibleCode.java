package com.kwang.climbstyle.code.inquiry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryVisibleCode {

    VISIBLE("Y", "노출"),
    HIDDEN("N", "숨김"),
    ;

    private final String code;

    private final String description;
}
