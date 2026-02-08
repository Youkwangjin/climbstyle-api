package com.kwang.climbstyle.domain.feed.repository;

import com.kwang.climbstyle.domain.feed.dto.response.FeedCommentListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedCommentRepository {

    List<FeedCommentListResponse> selectFeedCommentsByFeedNo(@Param("feedNo") Integer feedNo);

    Integer selectFeedCommentsCountByFeedNo(@Param("feedNo") Integer feedNo);
}
