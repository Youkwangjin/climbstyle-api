package com.kwang.climbstyle.domain.ranking.repository;

import com.kwang.climbstyle.common.protocal.PaginationRequest;
import com.kwang.climbstyle.domain.ranking.dto.response.RankingFeedResponse;
import com.kwang.climbstyle.domain.ranking.dto.response.RankingListResponse;
import com.kwang.climbstyle.domain.ranking.entity.RankingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RankingRepository {

    Integer selectRankingListCountByRequest(PaginationRequest request, @Param("rankingType") String rankingType);

    List<RankingListResponse> selectRankingList(PaginationRequest request, @Param("rankingType") String rankingType);

    List<RankingListResponse> selectIndexRealtime(@Param("rankingType") String rankingType);

    List<RankingEntity> selectRankingByType(@Param("rankingType") String rankingType);

    List<RankingFeedResponse> selectRealTimeRankingFeedByLikeCount(@Param("rankingTypeLimit") Integer rankingTypeLimit);

    List<RankingFeedResponse> selectPeriodRankingFeedByLikeCount(Integer rankingTypeLimit, LocalDateTime startDate);

    void insert(RankingEntity rankingEntity);

    void delete(@Param("rankingType") String rankingType);
}
