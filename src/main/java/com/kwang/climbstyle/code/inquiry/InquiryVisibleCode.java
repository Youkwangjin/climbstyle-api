package com.kwang.climbstyle.code.inquiry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 문의 노출 상태 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum InquiryVisibleCode {

    VISIBLE("Y", "노출"),
    HIDDEN("N", "숨김"),
    ;

    private final String code;

    private final String description;
}
