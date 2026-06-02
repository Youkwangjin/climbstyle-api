package com.kwang.climbstyle.domain.feed.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FeedCommentListResponse {

    private Integer feedCommentNo;

    private Integer feedCommentParentNo;

    private Integer userNo;

    private String userNickname;

    private String userImageUrl;

    private String feedCommentContent;

    private LocalDateTime feedCommentCreated;
}
