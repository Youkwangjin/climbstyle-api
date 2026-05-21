package com.kwang.climbstyle.domain.user.service;

import com.kwang.climbstyle.code.user.UserStatus;
import com.kwang.climbstyle.domain.admin.service.AdminUserMailService;
import com.kwang.climbstyle.domain.role.repository.UserRoleRepository;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 사용자 배치 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserBatchService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final AdminUserMailService adminUserMailService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 정지 기간 만료 사용자 자동 정지 해제
     */
    @Retryable(retryFor = {DataAccessException.class, RuntimeException.class}, backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 10000))
    @Transactional
    public void unsuspendExpiredUsers() {
        final LocalDateTime batchStarted = LocalDateTime.now();

        log.info("========== 정지 만료 사용자 자동 해제 배치 작업 시작 : {} ==========", batchStarted.format(FORMATTER));
        log.info("");

        int processedCount = 0;

        try {
            log.info("정지 만료 대상 사용자 조회");
            List<UserEntity> expiredUsers = userRepository.selectExpiredSuspendedUserList();
            log.info("정지 만료 대상 사용자 수: {}", expiredUsers.size());

            final String activeCode = UserStatus.ACTIVE.getCode();

            for (UserEntity user : expiredUsers) {
                final Integer userNo = user.getUserNo();

                log.info("정지 해제 처리 - userNo: {}", userNo);

                UserEntity unsuspendedUser = UserEntity.builder()
                        .userNo(userNo)
                        .userStatus(activeCode)
                        .userUpdated(LocalDateTime.now())
                        .build();

                userRepository.unsuspendUser(unsuspendedUser);
                adminUserMailService.sendUnsuspendMail(user);

                processedCount++;
            }

            log.info("");
            log.info("========== 정지 만료 사용자 자동 해제 배치 작업 완료 - 처리 건수: {} ==========", processedCount);

        } catch (Exception e) {
            log.error("");
            log.error("========== 정지 만료 사용자 자동 해제 배치 실패 ==========", e);
            throw e;
        }
    }

    /**
     * 탈퇴 후 30일 경과 사용자 데이터 삭제
     */
    @Retryable(retryFor = {DataAccessException.class, RuntimeException.class}, backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 10000))
    @Transactional
    public void deleteWithdrawnUsers() {
        final LocalDateTime batchStarted = LocalDateTime.now();

        log.info("========== 탈퇴 사용자 데이터 삭제 배치 작업 시작 : {} ==========", batchStarted.format(FORMATTER));
        log.info("");

        int processedCount = 0;

        try {
            log.info("삭제 대상 탈퇴 사용자 조회 (탈퇴 후 30일 경과)");
            List<UserEntity> withdrawnUsers = userRepository.selectWithdrawnUserList();
            log.info("삭제 대상 사용자 수: {}", withdrawnUsers.size());

            for (UserEntity user : withdrawnUsers) {
                final Integer userNo = user.getUserNo();

                log.info("사용자 데이터 삭제 처리 - userNo: {}, userWithdrawn: {}", userNo, user.getUserWithdrawn().format(FORMATTER));

                log.info("tb_user_role 데이터 삭제 - userNo: {}", userNo);
                userRoleRepository.deleteByUserNo(userNo);

                log.info("tb_user 데이터 삭제 - userNo: {}", userNo);
                userRepository.deleteByUserNo(userNo);

                processedCount++;
            }

            log.info("");
            log.info("========== 탈퇴 사용자 데이터 삭제 배치 작업 완료 - 처리 건수: {} ==========", processedCount);

        } catch (Exception e) {
            log.error("");
            log.error("========== 탈퇴 사용자 데이터 삭제 배치 실패 ==========", e);
            throw e;
        }
    }
}
