package com.kwang.climbstyle.domain.feed.repository;

import com.kwang.climbstyle.domain.feed.entity.FeedFileEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedFileRepository {

    void insert(FeedFileEntity feedFileEntity);
}
