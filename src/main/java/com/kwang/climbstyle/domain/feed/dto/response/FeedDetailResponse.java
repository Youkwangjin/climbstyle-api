package com.kwang.climbstyle.domain.feed.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FeedDetailResponse {

    private Integer feedNo;

    private Integer userNo;

    private String feedTitle;

    private String feedContent;

    private String feedVisibleYn;

    private String feedLikeVisibleYn;

    private List<String> feedFilePaths;

    private String userImageUrl;

    private String userNickName;

    private Integer feedLikeCount;

    private List<FeedCommentListResponse> feedCommentList;

    private Integer feedCommentCount;

    private LocalDateTime feedCreated;

    private Boolean isAuthor;

    private Boolean isLiked;
}
