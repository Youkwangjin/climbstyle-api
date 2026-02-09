package com.kwang.climbstyle.domain.feed.repository;

import com.kwang.climbstyle.domain.feed.entity.FeedLikeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FeedLikeRepository {

    Integer selectFeedLikeCountByFeedNo(@Param("feedNo") Integer feedNo);

    Boolean existFeedLikeByFeedNoAndUserNo(@Param("feedNo") Integer feedNo, @Param("userNo") Integer userNo);

    void insert(FeedLikeEntity feedLikeEntity);

    void delete(@Param("feedNo") Integer feedNo, @Param("userNo") Integer userNo);
}
