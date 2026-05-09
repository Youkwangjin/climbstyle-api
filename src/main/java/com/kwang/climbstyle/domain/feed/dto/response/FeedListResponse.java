package com.kwang.climbstyle.domain.feed.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FeedListResponse {

    private String userImageUrl;

    private String userNickName;

    private Integer feedNo;

    private String feedTitle;

    private String feedFilePath;

    private String feedLikeVisibleYn;

    private Integer feedLikeCount;

    private Integer feedCommentCount;

    private LocalDateTime feedCreated;
}
