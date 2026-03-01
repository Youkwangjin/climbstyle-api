package com.kwang.climbstyle.domain.ranking.scheduler;

import com.kwang.climbstyle.domain.ranking.service.RankingBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankingScheduler {

    private final RankingBatchService rankingBatchService;

    @Scheduled(cron = "0 0 * * * *")
    public void updateRealtimeRanking() {
        rankingBatchService.updateRealtimeRanking();
    }

    @Scheduled(cron = "20 0 0 * * *")
    public void updateWeeklyRanking() {
        rankingBatchService.updateWeeklyRanking();
    }
}
