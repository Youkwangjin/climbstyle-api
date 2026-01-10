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
public class FeedEntity {

    private Integer feedNo;

    private Integer userNo;

    private String feedTitle;

    private String feedContent;

    private String feedVisibleYn;

    private LocalDateTime feedCreated;

    private LocalDateTime feedUpdated;
}
