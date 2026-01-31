package com.kwang.climbstyle.domain.feed.repository;

import com.kwang.climbstyle.common.protocal.PaginationRequest;
import com.kwang.climbstyle.domain.feed.entity.FeedEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedRepository {

    Integer selectListCountByRequest(PaginationRequest request);

    List<FeedEntity> selectFeedList(PaginationRequest request);

    void insert(FeedEntity feedEntity);
}
