package com.kwang.climbstyle.domain.feed.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedCursorRequest {

    private Integer cursor;

    private int size = 9;
}
