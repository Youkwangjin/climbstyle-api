package com.kwang.climbstyle.code.ranking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 랭킹 타입 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum RankingType {

    REALTIME("REALTIME", "실시간 랭킹", "/rankings/realtime", 100),
    WEEKLY("WEEKLY", "주간 랭킹", "/rankings/weekly", 100),
    MONTHLY("MONTHLY", "월간 랭킹", "/rankings/monthly", 100),
    ;

    private final String code;

    private final String description;

    private final String url;

    private final Integer limit;

    public String getShortDescription() {
        return StringUtils.replace(this.description, " 랭킹", "");

    }
}
