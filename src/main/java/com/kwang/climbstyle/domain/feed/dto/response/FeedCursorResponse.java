package com.kwang.climbstyle.domain.feed.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FeedCursorResponse {

    private List<FeedListResponse> feeds;

    private Integer nextCursor;

    private boolean hasNext;
}
