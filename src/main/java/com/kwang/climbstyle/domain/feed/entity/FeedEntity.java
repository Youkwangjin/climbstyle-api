package com.kwang.climbstyle.domain.feed.entity;

import com.kwang.climbstyle.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedEntity {

    private Integer feedNo;

    private Integer userNo;

    private String feedTitle;

    private String feedContent;

    private String feedVisibleYn;

    private LocalDateTime feedCreated;

    private LocalDateTime feedUpdated;

    private List<FeedFileEntity> feedFiles;

    private List<FeedLikeEntity> feedLikes;

    private UserEntity user;
}
