package com.kwang.climbstyle.domain.feed.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FeedLikeRepository {

    Integer selectFeedLikeCountByFeedNo(@Param("feedNo") Integer feedNo);
}
