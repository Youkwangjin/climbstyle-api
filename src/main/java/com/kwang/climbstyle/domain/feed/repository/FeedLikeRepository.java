package com.kwang.climbstyle.domain.feed.repository;

import com.kwang.climbstyle.domain.feed.dto.response.FeedLikeDetailResponse;
import com.kwang.climbstyle.domain.feed.entity.FeedLikeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedLikeRepository {

    Boolean existFeedLikeByFeedNoAndUserNo(@Param("feedNo") Integer feedNo, @Param("userNo") Integer userNo);

    Integer selectFeedLikeCountByFeedNo(@Param("feedNo") Integer feedNo);

    List<FeedLikeDetailResponse> selectFeedLikeListByNo(@Param("feedNo") Integer feedNo);

    Integer insert(FeedLikeEntity feedLikeEntity);

    void delete(@Param("feedNo") Integer feedNo, @Param("userNo") Integer userNo);
}
