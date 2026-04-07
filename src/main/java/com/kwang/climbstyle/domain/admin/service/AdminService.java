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

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    private final UserRepository userRepository;

    private final InquiryRepository inquiryRepository;

    private final NoticeRepository noticeRepository;

    public AdminDashboardStatResponse getDashboardStat() {
        return adminRepository.selectDashboardStat();
    }

    public List<AdminUserListResponse> getUserList() {
        return userRepository.selectRecentUserList();
    }

    public List<AdminInquiryListResponse> getInquiryList(){
        return inquiryRepository.selectPendingInquiryList();
    }

    public List<AdminNoticeListResponse> getNoticeList() {
        return noticeRepository.selectRecentNoticeList();
    }
}
