package com.kwang.climbstyle.domain.ranking.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingHistoryEntity {

    private Integer rankingHistoryNo;

    private String rankingHistoryType;

    private String rankingHistoryStatus;

    private LocalDateTime rankingHistoryStarted;

    private LocalDateTime rankingHistoryEnded;

    private Integer rankingHistoryExecutionTime;

    private Integer rankingHistoryProcessedCount;

    private LocalDateTime rankingHistoryCreated;

}
