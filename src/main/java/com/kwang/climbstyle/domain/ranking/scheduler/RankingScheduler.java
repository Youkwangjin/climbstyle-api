package com.kwang.climbstyle.domain.ranking.scheduler;

import com.kwang.climbstyle.domain.ranking.service.RankingBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 랭킹 스케줄러
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Component
@RequiredArgsConstructor
public class RankingScheduler {

    private final RankingBatchService rankingBatchService;

    /**
     * 실시간 랭킹 갱신 스케줄 (매 정시)
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void updateRealtimeRanking() {
        rankingBatchService.updateRealtimeRanking();
    }

    /**
     * 주간 랭킹 갱신 스케줄 (매일 00:00:20)
     */
    @Scheduled(cron = "20 0 0 * * *")
    public void updateWeeklyRanking() {
        rankingBatchService.updateWeeklyRanking();
    }

    /**
     * 월간 랭킹 갱신 스케줄 (매월 1일 00:00:05)
     */
    @Scheduled(cron = "5 0 0 1 * *")
    public void updateMonthlyRanking() {
        rankingBatchService.updateMonthlyRanking();
    }
}
