package com.kwang.climbstyle.domain.user.service;

import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.domain.admin.dto.request.AdminUserListRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminUserListResponse;
import com.kwang.climbstyle.domain.user.dto.response.UserProfileResponse;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자 공통 조회 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 관리자 사용자 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdminUserListResponse> getAdminUserList(AdminUserListRequest request) {
        request.setTotalCount(userRepository.selectAdminUserListCountByRequest(request));

        return userRepository.selectAdminUserList(request);
    }

    /**
     * 사용자 번호로 프로필 조회
     */
    public UserProfileResponse selectUserByNo(Integer userNo) {
        UserEntity data = userRepository.selectUserByNo(userNo);
        if (data == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String userNm = data.getUserNm();
        final String userEmail = data.getUserEmail();
        final String userNickname = data.getUserNickname();
        final String userStatus = data.getUserStatus();
        final String userOauthProvider = data.getUserOauthProvider();
        final String userImageUrl = data.getUserImageUrl();
        final String userIntro = data.getUserIntro();
        final LocalDateTime userCreated = data.getUserCreated();
        final LocalDateTime userUpdated = data.getUserUpdated();
        final LocalDateTime userDeactivated = data.getUserDeactivated();

        return UserProfileResponse.builder()
                .userNo(userNo)
                .userNm(userNm)
                .userEmail(userEmail)
                .userNickname(userNickname)
                .userStatus(userStatus)
                .userOauthProvider(userOauthProvider)
                .userImgUrl(userImageUrl)
                .userIntro(userIntro)
                .userCreated(userCreated)
                .userUpdated(userUpdated)
                .userDeactivated(userDeactivated)
                .build();
    }
}
