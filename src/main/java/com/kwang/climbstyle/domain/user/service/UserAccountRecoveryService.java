package com.kwang.climbstyle.domain.user.service;

import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.domain.user.dto.request.UserFindIdRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserFindPwRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserResetPasswordRequest;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 사용자 아이디·비밀번호 찾기 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class UserAccountRecoveryService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 아이디 찾기 (마스킹 처리)
     */
    @Transactional(readOnly = true)
    public String findUserId(UserFindIdRequest request) {
        final String userNm = request.getUserNm();
        final String userEmail = request.getUserEmail();

        UserEntity user = userRepository.selectUserByNmAndEmail(userNm, userEmail);
        if (user == null) {
            throw new ClimbStyleException(UserErrorCode.USER_FIND_ID_NOT_FOUND);
        }

        String userId = user.getUserId();
        int len = userId.length();
        if (len <= 2) return userId;
        if (len <= 4) return userId.charAt(0) + "*".repeat(len - 1);

        return userId.substring(0, 2) + "*".repeat(len - 4) + userId.substring(len - 2);
    }

    /**
     * 비밀번호 재설정 대상 사용자 검증
     */
    @Transactional(readOnly = true)
    public void validateUserForPwReset(UserFindPwRequest request) {
        final String userId = request.getUserId();
        final String userEmail = request.getUserEmail();

        UserEntity user = userRepository.selectUserById(userId);
        if (user == null) {
            throw new ClimbStyleException(UserErrorCode.USER_FIND_PW_NOT_FOUND);
        }

        final String currentUserEmail = user.getUserEmail();
        final String userOauthProvider = user.getUserOauthProvider();

        if (userOauthProvider != null || !StringUtils.equals(currentUserEmail, userEmail)) {
            throw new ClimbStyleException(UserErrorCode.USER_FIND_PW_NOT_FOUND);
        }
    }

    /**
     * 비밀번호 재설정
     */
    @Transactional
    public void resetPassword(UserResetPasswordRequest request) {
        final String userEmail = request.getUserEmail();
        final String userNewPassword = request.getUserNewPassword();
        final String userPassword = passwordEncoder.encode(userNewPassword);
        final LocalDateTime userUpdated = LocalDateTime.now();

        UserEntity user = userRepository.selectNonOAuthUserByEmail(userEmail);
        if (user == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final Integer userNo = user.getUserNo();

        UserEntity userEntity = UserEntity.builder()
                .userNo(userNo)
                .userPassword(userPassword)
                .userUpdated(userUpdated)
                .build();

        userRepository.updatePassword(userEntity);
    }

    /**
     * 이메일로 아이디 조회
     */
    @Transactional(readOnly = true)
    public String getUserIdByEmail(String userEmail) {
        UserEntity user = userRepository.selectNonOAuthUserByEmail(userEmail);
        if (user == null) {
            throw new ClimbStyleException(UserErrorCode.USER_FIND_ID_NOT_FOUND);
        }
        return user.getUserId();
    }
}
