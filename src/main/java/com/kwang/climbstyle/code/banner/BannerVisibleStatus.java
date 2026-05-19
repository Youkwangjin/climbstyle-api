package com.kwang.climbstyle.code.banner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BannerVisibleStatus {

    VISIBLE("Y", "공개"),
    HIDDEN("N", "비공개"),
    ;

    private final String code;

    private final String description;
}
