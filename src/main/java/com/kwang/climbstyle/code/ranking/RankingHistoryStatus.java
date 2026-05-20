package com.kwang.climbstyle.code.ranking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 랭킹 배치 이력 상태 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum RankingHistoryStatus {

    SUCCESS("SUCCESS", "성공"),
    FAILED("FAILED", "실패"),
    ;

    private final String code;

    private final String description;
}
