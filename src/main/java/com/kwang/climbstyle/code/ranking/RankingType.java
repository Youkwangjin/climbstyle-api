package com.kwang.climbstyle.code.ranking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RankingType {

    REALTIME("REALTIME", "실시간"),
    WEEKLY("WEEKLY", "주간"),
    MONTHLY("MONTHLY", "월간"),
    ;

    private final String code;

    private final String description;
}
