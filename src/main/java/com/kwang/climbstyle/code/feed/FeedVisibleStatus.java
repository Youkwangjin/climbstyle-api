package com.kwang.climbstyle.code.feed;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedVisibleStatus {

    VISIBLE("Y", "공개"),
    HIDDEN("N", "비공개"),
    ;

    private final String code;

    private final String description;
}
