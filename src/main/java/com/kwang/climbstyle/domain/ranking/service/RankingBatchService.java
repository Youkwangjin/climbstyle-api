package com.kwang.climbstyle.domain.ranking.service;

import com.kwang.climbstyle.code.ranking.RankingHistoryStatus;
import com.kwang.climbstyle.code.ranking.RankingType;
import com.kwang.climbstyle.domain.ranking.dto.response.RankingFeedResponse;
import com.kwang.climbstyle.domain.ranking.entity.RankingEntity;
import com.kwang.climbstyle.domain.ranking.entity.RankingHistoryEntity;
import com.kwang.climbstyle.domain.ranking.repository.RankingHistoryRepository;
import com.kwang.climbstyle.domain.ranking.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final RankingHistoryRepository rankingHistoryRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Retryable(retryFor = {DataAccessException.class, RuntimeException.class}, backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 10000))
    @Transactional
    public void updateRealtimeRanking() {
        final LocalDateTime rankingHistoryStarted = LocalDateTime.now();
        final long startTimeMs = System.currentTimeMillis();
        final String rankingType = RankingType.REALTIME.getCode();
        final Integer rankingTypeLimit = RankingType.REALTIME.getLimit();

        log.info("========== 실시간 랭킹 배치 작업 시작 : {} ==========", rankingHistoryStarted.format(FORMATTER));
        log.info("");

        String rankingHistoryStatus = RankingHistoryStatus.SUCCESS.getCode();
        int rankingHistoryProcessedCount = 0;

        try {
            log.info("기존 랭킹 데이터 조회");
            List<RankingEntity> rankingEntityList = rankingRepository.selectRankingByType(rankingType);
            log.info("기존 랭킹 개수: {}", rankingEntityList.size());

            Map<Integer, Integer> previousRankMap = new HashMap<>();
            for (RankingEntity rankingEntity : rankingEntityList) {
                previousRankMap.put(rankingEntity.getFeedNo(), rankingEntity.getRankingOrder());
            }

            log.info("좋아요 수 기준 새 랭킹 계산");
            List<RankingFeedResponse> feedResponses = rankingRepository.selectRankingFeedByLikeCount(rankingTypeLimit);
            log.info("새 랭킹 개수: {}", feedResponses.size());

            List<RankingEntity> newRankingEntityList = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();

            for (int i = 0; i < feedResponses.size(); i++) {
                RankingFeedResponse feedResponse = feedResponses.get(i);
                int currentRank = i + 1;
                Integer previousRank = previousRankMap.get(feedResponse.getFeedNo());

                RankingEntity rankingEntity = RankingEntity.builder()
                        .feedNo(feedResponse.getFeedNo())
                        .rankingType(rankingType)
                        .rankingOrder(currentRank)
                        .rankingLikeCount(feedResponse.getFeedLikeCount())
                        .rankingPreviousOrder(previousRank)
                        .rankingUpdated(now)
                        .build();

                newRankingEntityList.add(rankingEntity);
            }

            log.info("기존 랭킹 데이터 삭제");
            rankingRepository.delete(rankingType);
            log.info("삭제된 랭킹 타입: {}", rankingType);

            log.info("새 랭킹 데이터 삽입");
            for (RankingEntity rankingEntity : newRankingEntityList) {
                rankingRepository.insert(rankingEntity);
            }
            log.info("삽입된 랭킹 개수: {}", newRankingEntityList.size());

            rankingHistoryProcessedCount = newRankingEntityList.size();

            log.info("");
            log.info("========== 실시간 랭킹 배치 작업 완료 ==========");
        } catch (Exception e) {
            rankingHistoryStatus = RankingHistoryStatus.FAILED.getCode();

            log.error("");
            log.error("========== 실시간 랭킹 배치 실패 ==========", e);
            throw e;
        } finally {
            saveHistory(rankingType, rankingHistoryStatus, rankingHistoryStarted, startTimeMs, rankingHistoryProcessedCount);
        }
    }

    private void saveHistory(String rankingType, String rankingHistoryStatus, LocalDateTime rankingHistoryStarted,
                             long startTimeMs, int rankingHistoryProcessedCount) {

        final LocalDateTime rankingHistoryEnded = LocalDateTime.now();
        final LocalDateTime rankingHistoryCreated = LocalDateTime.now();
        final long executionTimeMs = System.currentTimeMillis() - startTimeMs;
        final int rankingHistoryExecutionTime = (int) executionTimeMs;

        RankingHistoryEntity rankingHistoryEntity = RankingHistoryEntity.builder()
                .rankingHistoryType(rankingType)
                .rankingHistoryStatus(rankingHistoryStatus)
                .rankingHistoryStarted(rankingHistoryStarted)
                .rankingHistoryEnded(rankingHistoryEnded)
                .rankingHistoryExecutionTime(rankingHistoryExecutionTime)
                .rankingHistoryProcessedCount(rankingHistoryProcessedCount)
                .rankingHistoryCreated(rankingHistoryCreated)
                .build();

        rankingHistoryRepository.insert(rankingHistoryEntity);
    }
}
