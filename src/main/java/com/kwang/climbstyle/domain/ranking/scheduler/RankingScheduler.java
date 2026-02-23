package com.kwang.climbstyle.domain.ranking.scheduler;

import com.kwang.climbstyle.domain.ranking.service.RankingBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RankingScheduler {

    private final RankingBatchService rankingBatchService;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void updateRanking() {
        rankingBatchService.updateRealtimeRanking();
    }
}
