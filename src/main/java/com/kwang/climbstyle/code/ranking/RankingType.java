package com.kwang.climbstyle.code.ranking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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
