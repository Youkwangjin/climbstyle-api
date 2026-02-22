package com.kwang.climbstyle.domain.ranking.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RankingListResponse {

    private Integer rankingNo;

    private Integer rankingOrder;

    private Integer rankingLikeCount;

    private Integer rankingPreviousOrder;

    private LocalDateTime rankingUpdated;

    private Integer feedNo;

    private String feedTitle;

    private Integer userNo;

    private String userNickname;

    private String userImageUrl;

    private Integer feedFileNo;

    private String feedFilePath;
}
