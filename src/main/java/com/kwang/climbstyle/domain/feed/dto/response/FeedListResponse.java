package com.kwang.climbstyle.domain.feed.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FeedListResponse {

    private Integer feedNo;

    private String feedTitle;

    private String userNm;

    private Integer feedLikeCount;

    private Integer feedCommentCount;

    private LocalDateTime feedCreated;
}
