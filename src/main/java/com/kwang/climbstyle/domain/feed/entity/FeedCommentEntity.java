package com.kwang.climbstyle.domain.feed.entity;

import com.kwang.climbstyle.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedCommentEntity {

    private Integer feedCommentNo;

    private Integer feedNo;

    private Integer userNo;

    private Integer feedCommentParentNo;

    private String feedCommentContent;

    private String feedCommentDeleteYn;

    private LocalDateTime feedCommentCreated;

    private LocalDateTime feedCommentUpdated;

    private UserEntity user;
}
