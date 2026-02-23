package com.kwang.climbstyle.code.ranking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RankingType {

    REALTIME("REALTIME", "실시간", 100),
    WEEKLY("WEEKLY", "주간", 100),
    MONTHLY("MONTHLY", "월간", 100),
    ;

    private final String code;

    private final String description;

    private final Integer limit;
}
