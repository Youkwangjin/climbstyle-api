package com.kwang.climbstyle.code.ranking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RankingHistoryStatus {

    SUCCESS("SUCCESS", "성공"),
    FAILED("FAILED", "실패"),
    ;

    private final String code;

    private final String description;
}
