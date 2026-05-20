package com.kwang.climbstyle.code.inquiry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 문의 삭제 상태 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum InquiryDeleteCode {

    DELETED("Y", "삭제"),
    NOT_DELETED("N", "미삭제"),
    ;

    private final String code;

    private final String description;
}
