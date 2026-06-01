package com.kwang.climbstyle.domain.feed.repository;

import com.kwang.climbstyle.domain.feed.dto.response.FeedDetailResponse;
import com.kwang.climbstyle.domain.feed.dto.response.FeedListResponse;
import com.kwang.climbstyle.domain.feed.entity.FeedEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedRepository {

    Boolean existFeedByNo(@Param("feedNo") Integer feedNo);

    Boolean existsFeedByNoAndUserNo(@Param("feedNo") Integer feedNo, @Param("userNo") Integer userNo);

    List<FeedListResponse> selectLatestFeeds(@Param("limit") int limit, @Param("userNo") Integer userNo);

    List<FeedListResponse> selectFeedListByCursor(@Param("cursor") Integer cursor, @Param("size") int size,
                                                  @Param("userNo") Integer userNo);

    List<FeedListResponse> selectMyFeedListByCursor(@Param("cursor") Integer cursor, @Param("size") int size,
                                                    @Param("userNo") Integer userNo);

    FeedDetailResponse selectFeedByNo(@Param("feedNo") Integer feedNo);

    void insert(FeedEntity feedEntity);

    void update(FeedEntity feedEntity);

    void updateFeedVisibleYnByUserNo(@Param("userNo") Integer userNo, @Param("feedVisibleYn") String feedVisibleYn);

    void delete(@Param("feedNo") Integer feedNo);

    void deleteByUserNo(@Param("userNo") Integer userNo);
}
