package com.kwang.climbstyle.domain.feed.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FeedListResponse {

    private String userImageUrl;

    private String userNickName;

    private Integer feedNo;

    private String feedTitle;

    private String feedFilePath;

    private Integer feedLikeCount;

    private Integer feedCommentCount;

    private LocalDateTime feedCreated;
}
