package com.kwang.climbstyle.domain.feed.repository;

import com.kwang.climbstyle.domain.feed.dto.response.FeedFileDetailResponse;
import com.kwang.climbstyle.domain.feed.entity.FeedFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedFileRepository {

    List<FeedFileDetailResponse> selectFeedFilesByFeedNo(@Param("feedNo") Integer feedNo);

    List<String> selectFeedFilePathsByFeedNo(@Param("feedNo") Integer feedNo);

    void insert(FeedFileEntity feedFileEntity);
}
