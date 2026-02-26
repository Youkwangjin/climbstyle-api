package com.kwang.climbstyle.domain.ranking.repository;

import com.kwang.climbstyle.domain.ranking.entity.RankingHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RankingHistoryRepository {

    void insert(RankingHistoryEntity rankingHistoryEntity);
}
