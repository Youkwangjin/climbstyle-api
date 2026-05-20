package com.kwang.climbstyle.code.banner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 배너 노출 상태 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum BannerVisibleStatus {

    VISIBLE("Y", "공개"),
    HIDDEN("N", "비공개"),
    ;

    private final String code;

    private final String description;
}
