package com.kwang.climbstyle.domain.feed.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedLikeEntity {

    private Integer feedLikeNo;

    private Integer feedNo;

    private Integer userNo;

    private String feedLikeVisibleYn;

    private LocalDateTime feedLikeCreated;

    private LocalDateTime feedLikeUpdated;
}
