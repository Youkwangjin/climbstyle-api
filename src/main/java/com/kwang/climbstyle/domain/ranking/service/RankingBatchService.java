package com.kwang.climbstyle.domain.ranking.service;

import com.kwang.climbstyle.code.ranking.RankingType;
import com.kwang.climbstyle.domain.ranking.dto.response.RankingFeedResponse;
import com.kwang.climbstyle.domain.ranking.entity.RankingEntity;
import com.kwang.climbstyle.domain.ranking.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankingBatchService {

    private final RankingRepository rankingRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void updateRealtimeRanking() {
        final long startTime = System.currentTimeMillis();
        final String rankingType = RankingType.REALTIME.getCode();
        final Integer rankingTypeLimit = RankingType.REALTIME.getLimit();

        log.info("========== Start Realtime Ranking Batch : {} ==========",
                LocalDateTime.now().format(FORMATTER));
        log.info("");

        log.info("Step 1: Loading previous ranking data...");
        List<RankingEntity> rankingEntityList = rankingRepository.selectRankingByType(rankingType);
        log.info("Previous ranking count: {}", rankingEntityList.size());

        Map<Integer, Integer> previousRankMap = new HashMap<>();
        for (RankingEntity rankingEntity : rankingEntityList) {
            previousRankMap.put(rankingEntity.getFeedNo(), rankingEntity.getRankingOrder());
        }

        log.info("Step 2: Calculating new ranking based on like count...");
        List<RankingFeedResponse> feedResponses = rankingRepository.selectRankingFeedByLikeCount(rankingTypeLimit);
        log.info("New ranking count: {}", feedResponses.size());

        log.info("Step 3: Creating ranking entities...");
        List<RankingEntity> newRankings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < feedResponses.size(); i++) {
            RankingFeedResponse feedResponse = feedResponses.get(i);
            int currnetRank = i + 1;

            Integer previousRank = previousRankMap.get(feedResponse.getFeedNo());

            RankingEntity rankingEntity = RankingEntity.builder()
                    .feedNo(feedResponse.getFeedNo())
                    .rankingType(rankingType)
                    .rankingOrder(currnetRank)
                    .rankingLikeCount(feedResponse.getFeedLikeCount())
                    .rankingPreviousOrder(previousRank)
                    .rankingUpdated(now)
                    .build();

            newRankings.add(rankingEntity);
        }

        log.info("Step 4: Deleting previous ranking data...");
        rankingRepository.deleteBatch(rankingType);
        log.info("Deleted ranking type: {}", rankingType);

        log.info("Step 5: Inserting new ranking data...");
        for (RankingEntity ranking : newRankings) {
            rankingRepository.insertBatch(ranking);
        }
        log.info("Inserted {} rankings", newRankings.size());

        final long endTime = System.currentTimeMillis();
        final long executionTime = endTime - startTime;

        log.info("");
        log.info("========== Complete Realtime Ranking Batch ==========");
        log.info("Execution time: {} ms ({} sec)", executionTime, executionTime / 1000.0);
        log.info("Updated at: {}", now.format(FORMATTER));
        log.info("=====================================================");
        log.info("");
    }
}
