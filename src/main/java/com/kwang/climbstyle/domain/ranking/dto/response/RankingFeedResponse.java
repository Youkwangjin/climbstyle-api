package com.kwang.climbstyle.domain.ranking.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RankingFeedResponse {

    private Integer feedNo;

    private Integer feedLikeCount;

    private LocalDateTime feedCreated;
}
