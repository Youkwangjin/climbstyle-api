package com.kwang.climbstyle.domain.feed.repository;

import com.kwang.climbstyle.common.protocal.PaginationRequest;
import com.kwang.climbstyle.domain.feed.entity.FeedEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedRepository {

    Integer selectFeedListCountByRequest(PaginationRequest request);

    Integer selectMyFeedListCountByRequest(PaginationRequest request, @Param("userNo") Integer userNo);

    List<FeedEntity> selectFeedList(PaginationRequest request);

    List<FeedEntity> selectMyFeedList(PaginationRequest request, @Param("userNo") Integer userNo);

    void insert(FeedEntity feedEntity);
}
