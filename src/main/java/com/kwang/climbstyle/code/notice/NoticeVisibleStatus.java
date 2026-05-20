package com.kwang.climbstyle.code.notice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 공지사항 노출 상태 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum NoticeVisibleStatus {

    VISIBLE("Y", "공개"),
    HIDDEN("N", "비공개"),
    ;

    private final String code;

    private final String description;
}
