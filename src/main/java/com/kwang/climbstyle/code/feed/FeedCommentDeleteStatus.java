package com.kwang.climbstyle.code.feed;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedCommentDeleteStatus {

    NOT_DELETED("N", "미삭제"),
    DELETED("Y", "삭제"),
    ;

    private final String code;

    private final String description;
}
