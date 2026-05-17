package com.kwang.climbstyle.domain.admin.service;

import com.kwang.climbstyle.code.feed.FeedVisibleStatus;
import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.code.user.UserSuspendCategory;
import com.kwang.climbstyle.code.user.UserStatus;
import com.kwang.climbstyle.domain.admin.dto.request.AdminUserSuspendRequest;
import com.kwang.climbstyle.domain.feed.repository.FeedCommentRepository;
import com.kwang.climbstyle.domain.feed.repository.FeedRepository;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 관리자 사용자 관리 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-17
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    private final FeedRepository feedRepository;

    private final FeedCommentRepository feedCommentRepository;

    private final AdminUserMailService adminUserMailService;

    /**
     * 사용자 정지 처리
     */
    @Transactional
    public void suspendUser(Integer userNo, AdminUserSuspendRequest request) {
        final UserSuspendCategory suspendCategory = request.getSuspendCategory();
        final String suspendReason = request.getSuspendReason();
        final Integer suspendDays = request.getSuspendDays();

        UserEntity user = userRepository.selectUserByNo(userNo);
        if (user == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String currentUserStatus = user.getUserStatus();
        final String suspendedCode = UserStatus.SUSPENDED.getCode();

        if (StringUtils.equals(currentUserStatus, suspendedCode)) {
            throw new ClimbStyleException(UserErrorCode.USER_ALREADY_SUSPENDED);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime userSuspendUntil = null;
        if (suspendDays != null) {
            userSuspendUntil = now.plusDays(suspendDays);
        }

        final String userSuspendCategory = suspendCategory.getCode();

        UserEntity suspendedUser = UserEntity.builder()
                .userNo(userNo)
                .userStatus(suspendedCode)
                .userSuspendReason(suspendReason)
                .userSuspendCategory(userSuspendCategory)
                .userSuspended(now)
                .userSuspendUntil(userSuspendUntil)
                .build();

        userRepository.suspendUser(suspendedUser);

        final String feedVisibleYn = FeedVisibleStatus.HIDDEN.getCode();
        feedRepository.updateFeedVisibleYnByUserNo(userNo, feedVisibleYn);

        final String feedCommentVisibleYn = FeedVisibleStatus.HIDDEN.getCode();
        feedCommentRepository.updateFeedCommentVisibleYnByUserNo(userNo, feedCommentVisibleYn);

        adminUserMailService.sendSuspendMail(user, suspendCategory, suspendReason, userSuspendUntil);
    }

    /**
     * 사용자 정지 취소 처리
     */
    @Transactional
    public void unsuspendUser(Integer userNo) {
        UserEntity user = userRepository.selectUserByNo(userNo);
        if (user == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String currentUserStatus = user.getUserStatus();
        final String suspendedCode = UserStatus.SUSPENDED.getCode();

        if (!StringUtils.equals(currentUserStatus, suspendedCode)) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_SUSPENDED);
        }

        final String activeCode = UserStatus.ACTIVE.getCode();

        UserEntity unsuspendedUser = UserEntity.builder()
                .userNo(userNo)
                .userStatus(activeCode)
                .userUpdated(LocalDateTime.now())
                .build();

        userRepository.unsuspendUser(unsuspendedUser);

        adminUserMailService.sendUnsuspendMail(user);
    }
}
