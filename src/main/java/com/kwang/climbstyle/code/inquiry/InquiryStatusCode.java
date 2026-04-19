package com.kwang.climbstyle.code.inquiry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryStatusCode {

    PENDING("PENDING",     "대기"),
    PROGRESS("PROGRESS",   "처리중"),
    COMPLETED("COMPLETED", "완료"),
    CANCELLED("CANCELLED", "취소"),
    ;

    private final String code;

    private final String description;
}
