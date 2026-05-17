package com.kwang.climbstyle.domain.admin.service;

import com.kwang.climbstyle.domain.admin.entity.AdminLoginHistoryEntity;
import com.kwang.climbstyle.domain.admin.repository.AdminHistoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 관리자 로그인 이력 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class AdminHistoryService {

    private final AdminHistoryRepository adminHistoryRepository;

    /**
     * 로그인 성공 이력 저장
     */
    @Transactional
    public void saveSuccess(Integer adminNo, HttpServletRequest request) {
        final String adminLoginHistorySuccessYn = "Y";
        final String adminLoginHistoryIp = request.getRemoteAddr();
        final String adminLoginHistoryUserAgent =  request.getHeader("User-Agent");
        final LocalDateTime adminLoginHistoryAt =  LocalDateTime.now();

        AdminLoginHistoryEntity adminLoginHistoryEntity = AdminLoginHistoryEntity.builder()
                .adminNo(adminNo)
                .adminLoginHistorySuccessYn(adminLoginHistorySuccessYn)
                .adminLoginHistoryIp(adminLoginHistoryIp)
                .adminLoginHistoryUserAgent(adminLoginHistoryUserAgent)
                .adminLoginHistoryAt(adminLoginHistoryAt)
                .build();

        adminHistoryRepository.insert(adminLoginHistoryEntity);
    }

    /**
     * 로그인 실패 이력 저장
     */
    @Transactional
    public void saveFailure(String failId, HttpServletRequest request) {
        final String adminLoginHistorySuccessYn = "N";
        final String adminLoginHistoryIp = request.getRemoteAddr();
        final String adminLoginHistoryUserAgent =  request.getHeader("User-Agent");
        final LocalDateTime adminLoginHistoryAt =  LocalDateTime.now();

        AdminLoginHistoryEntity adminLoginHistoryEntity = AdminLoginHistoryEntity.builder()
                .adminLoginHistoryFailId(failId)
                .adminLoginHistorySuccessYn(adminLoginHistorySuccessYn)
                .adminLoginHistoryIp(adminLoginHistoryIp)
                .adminLoginHistoryUserAgent(adminLoginHistoryUserAgent)
                .adminLoginHistoryAt(adminLoginHistoryAt)
                .build();

        adminHistoryRepository.insert(adminLoginHistoryEntity);
    }
}
