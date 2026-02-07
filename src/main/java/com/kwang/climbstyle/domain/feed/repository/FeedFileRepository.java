package com.kwang.climbstyle.domain.feed.repository;

import com.kwang.climbstyle.domain.feed.entity.FeedFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedFileRepository {

    List<String> selectFeedFilesByFeedNo(@Param("feedNo") Integer feedNo);

    void insert(FeedFileEntity feedFileEntity);
}
