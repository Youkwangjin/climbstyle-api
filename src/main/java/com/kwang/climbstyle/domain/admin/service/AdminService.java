package com.kwang.climbstyle.domain.admin.service;

import com.kwang.climbstyle.domain.admin.dto.response.AdminDashboardStatResponse;
import com.kwang.climbstyle.domain.admin.dto.response.AdminInquiryListResponse;
import com.kwang.climbstyle.domain.admin.dto.response.AdminNoticeListResponse;
import com.kwang.climbstyle.domain.admin.dto.response.AdminUserListResponse;
import com.kwang.climbstyle.domain.admin.repository.AdminRepository;
import com.kwang.climbstyle.domain.inquiry.repository.InquiryRepository;
import com.kwang.climbstyle.domain.notice.repository.NoticeRepository;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 관리자 대시보드 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    private final UserRepository userRepository;

    private final InquiryRepository inquiryRepository;

    private final NoticeRepository noticeRepository;

    /**
     * 대시보드 통계 조회
     */
    @Transactional(readOnly = true)
    public AdminDashboardStatResponse getDashboardStat() {
        return adminRepository.selectDashboardStat();
    }

    /**
     * 최근 가입 사용자 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdminUserListResponse> getUserList() {
        return userRepository.selectRecentUserList();
    }

    /**
     * 대기 중인 문의 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdminInquiryListResponse> getInquiryList(){
        return inquiryRepository.selectPendingInquiryList();
    }

    /**
     * 최근 공지사항 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdminNoticeListResponse> getNoticeList() {
        return noticeRepository.selectRecentNoticeList();
    }
}
