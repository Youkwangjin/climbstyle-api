package com.kwang.climbstyle.domain.ranking.service;

import com.kwang.climbstyle.code.ranking.RankingType;
import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.domain.ranking.dto.response.RankingListResponse;
import com.kwang.climbstyle.domain.ranking.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 랭킹 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    /**
     * 실시간 랭킹 목록 조회
     */
    public List<RankingListResponse> getRealtimeRankingList(CommonListRequest request) {
        final String rankingType = RankingType.REALTIME.getCode();
        request.setTotalCount(rankingRepository.selectRankingListCountByRequest(request, rankingType));

        return rankingRepository.selectRankingList(request, rankingType);
    }

    /**
     * 홈 화면 실시간 랭킹 Top3 조회
     */
    public List<RankingListResponse> getIndexRealtimeRankingList() {
        final String rankingType = RankingType.REALTIME.getCode();

        return rankingRepository.selectIndexRealtime(rankingType);
    }

    /**
     * 주간 랭킹 목록 조회
     */
    public List<RankingListResponse> getWeeklyRankingList(CommonListRequest request) {
        final String rankingType = RankingType.WEEKLY.getCode();
        request.setTotalCount(rankingRepository.selectRankingListCountByRequest(request, rankingType));

        return rankingRepository.selectRankingList(request, rankingType);
    }

    /**
     * 월간 랭킹 목록 조회
     */
    public List<RankingListResponse> getMonthlyRankingList(CommonListRequest request) {
        final String rankingType = RankingType.MONTHLY.getCode();
        request.setTotalCount(rankingRepository.selectRankingListCountByRequest(request, rankingType));

        return rankingRepository.selectRankingList(request, rankingType);
    }
}
