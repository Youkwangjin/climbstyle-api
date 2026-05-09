package com.kwang.climbstyle.domain.user.scheduler;

import com.kwang.climbstyle.domain.user.service.UserBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 사용자 스케줄러
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@Component
@RequiredArgsConstructor
public class UserScheduler {

    private final UserBatchService userBatchService;

    /**
     * 탈퇴 후 30일 경과 사용자 데이터 삭제 (매일 새벽 4시)
     */
    @Scheduled(cron = "0 0 4 * * *")
    public void deleteWithdrawnUsers() {
        userBatchService.deleteWithdrawnUsers();
    }
}
