package com.kwang.climbstyle.domain.feed.dto.response;

import lombok.*;

@Getter
@Builder
public class FeedLikeResponse {

    private Boolean isLiked;

    private Integer feedLikeCount;
}
