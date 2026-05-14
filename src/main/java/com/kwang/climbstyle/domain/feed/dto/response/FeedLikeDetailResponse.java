package com.kwang.climbstyle.domain.feed.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedLikeDetailResponse {

    private Integer feedLikeNo;

    private String userNickname;

    private String userImageUrl;
}
