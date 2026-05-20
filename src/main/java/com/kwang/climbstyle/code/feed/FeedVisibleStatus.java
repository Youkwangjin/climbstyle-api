package com.kwang.climbstyle.code.feed;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 피드 공개 상태 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum FeedVisibleStatus {

    VISIBLE("Y", "공개"),
    HIDDEN("N", "비공개"),
    ;

    private final String code;

    private final String description;
}
