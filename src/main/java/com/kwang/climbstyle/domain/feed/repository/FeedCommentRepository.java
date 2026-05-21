package com.kwang.climbstyle.domain.feed.repository;

import com.kwang.climbstyle.domain.feed.dto.response.FeedCommentListResponse;
import com.kwang.climbstyle.domain.feed.entity.FeedCommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedCommentRepository {

    Boolean existsByFeedCommentNo(@Param("feedCommentNo") Integer feedCommentNo);

    List<FeedCommentListResponse> selectFeedCommentsByFeedNo(@Param("feedNo") Integer feedNo);

    Integer selectFeedCommentsCountByFeedNo(@Param("feedNo") Integer feedNo);

    void insert(FeedCommentEntity feedCommentEntity);

    void updateFeedCommentVisibleYnByUserNo(@Param("userNo") Integer userNo, @Param("feedCommentVisibleYn") String feedCommentVisibleYn);
}
