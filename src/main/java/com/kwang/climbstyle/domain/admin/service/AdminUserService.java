package com.kwang.climbstyle.domain.admin.service;

import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.code.user.UserSuspendCategory;
import com.kwang.climbstyle.code.user.UserStatus;
import com.kwang.climbstyle.domain.admin.dto.request.AdminUserSuspendRequest;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    private final AdminUserMailService adminUserMailService;

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

        adminUserMailService.sendSuspendMail(user, suspendCategory, suspendReason, userSuspendUntil);
    }
}
