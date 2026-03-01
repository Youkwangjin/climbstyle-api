package com.kwang.climbstyle.domain.ranking.service;

import com.kwang.climbstyle.code.ranking.RankingType;
import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.domain.ranking.dto.response.RankingListResponse;
import com.kwang.climbstyle.domain.ranking.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    public List<RankingListResponse> getRealtimeRankingList(CommonListRequest request) {
        final String rankingType = RankingType.REALTIME.getCode();
        request.setTotalCount(rankingRepository.selectRankingListCountByRequest(request, rankingType));

        return rankingRepository.selectRankingList(request, rankingType);
    }
}
