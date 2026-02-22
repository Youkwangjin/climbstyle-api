package com.kwang.climbstyle.domain.ranking.entity;

import com.kwang.climbstyle.domain.feed.entity.FeedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingEntity {

    private Integer rankingNo;

    private Integer feedNo;

    private String rankingType;

    private Integer rankingOrder;

    private Integer rankingLikeCount;

    private Integer rankingPreviousOrder;

    private LocalDateTime rankingUpdated;

    private FeedEntity feed;
}
