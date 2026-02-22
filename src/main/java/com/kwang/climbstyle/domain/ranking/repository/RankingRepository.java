package com.kwang.climbstyle.domain.ranking.repository;

import com.kwang.climbstyle.domain.ranking.dto.response.RankingListResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RankingRepository {
    List<RankingListResponse> selectRankingList(String rankingType);
}
