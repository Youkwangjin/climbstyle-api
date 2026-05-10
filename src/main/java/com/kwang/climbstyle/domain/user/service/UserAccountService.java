package com.kwang.climbstyle.domain.user.service;

import com.kwang.climbstyle.code.feed.FeedVisibleStatus;
import com.kwang.climbstyle.code.file.FileTypeCode;
import com.kwang.climbstyle.code.inquiry.InquiryVisibleCode;
import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.code.user.UserStatus;
import com.kwang.climbstyle.domain.feed.repository.FeedFileRepository;
import com.kwang.climbstyle.domain.feed.repository.FeedRepository;
import com.kwang.climbstyle.domain.file.service.FileService;
import com.kwang.climbstyle.domain.inquiry.repository.InquiryRepository;
import com.kwang.climbstyle.domain.user.dto.request.UserPasswordUpdateRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserReactivateRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserUpdateRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserWithdrawRequest;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import com.kwang.climbstyle.security.oauth2.response.OAuth2UserResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 사용자 계정 관리 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final FileService fileService;

    private final UserRepository userRepository;

    private final FeedRepository feedRepository;

    private final FeedFileRepository feedFileRepository;

    private final InquiryRepository inquiryRepository;

    private final PasswordEncoder passwordEncoder;

    public static final String DELETE_FLAG = "true";

    /**
     * 계정 휴면 처리
     */
    @Transactional
    public void deactivateUser(Integer userNo) {
        UserEntity data = userRepository.selectUserByNo(userNo);
        if (data == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String userStatus = data.getUserStatus();
        final String dormantStatus = UserStatus.DORMANT.getCode();
        final LocalDateTime currentUserDeactivated = data.getUserDeactivated();
        final LocalDateTime userDeactivated = LocalDateTime.now();
        final LocalDateTime now = LocalDateTime.now();

        if (StringUtils.equals(userStatus, dormantStatus)) {
            throw new ClimbStyleException(UserErrorCode.USER_ALREADY_DORMANT);
        }

        if (currentUserDeactivated != null) {
            LocalDateTime availableAt = currentUserDeactivated.plusDays(3);
            if (now.isBefore(availableAt)) {
                throw new ClimbStyleException(UserErrorCode.USER_DORMANCY_COOLDOWN);
            }
        }

        UserEntity userEntity = UserEntity.builder()
                .userNo(userNo)
                .userStatus(dormantStatus)
                .userDeactivated(userDeactivated)
                .build();

        userRepository.deactivateUser(userEntity);

        final String feedVisibleYn = FeedVisibleStatus.HIDDEN.getCode();
        final String inquiryVisibleYn = InquiryVisibleCode.HIDDEN.getCode();

        feedRepository.updateFeedVisibleYnByUserNo(userNo, feedVisibleYn);
        inquiryRepository.updateVisibleYnByUserNo(userNo, inquiryVisibleYn);
    }

    /**
     * 일반 계정 휴면 해제
     */
    @Transactional
    public void reactivateUser(UserReactivateRequest request) {
        final String userId = request.getUserId();
        final String userPassword = request.getUserPassword();

        UserEntity data = userRepository.selectUserById(userId);
        if (data == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(userPassword, data.getUserPassword())) {
            throw new ClimbStyleException(UserErrorCode.USER_PASSWORD_MISMATCH);
        }

        final Integer userNo = data.getUserNo();
        final String userStatus = data.getUserStatus();
        final String reactivateStatus = UserStatus.ACTIVE.getCode();

        if (StringUtils.equals(userStatus, reactivateStatus)) {
            throw new ClimbStyleException(UserErrorCode.USER_ALREADY_REACTIVATE);
        }

        UserEntity userEntity = UserEntity.builder()
                .userNo(userNo)
                .userStatus(reactivateStatus)
                .build();

        userRepository.reactivateUser(userEntity);

        final String feedVisibleYn = FeedVisibleStatus.VISIBLE.getCode();
        final String inquiryVisibleYn = InquiryVisibleCode.VISIBLE.getCode();

        feedRepository.updateFeedVisibleYnByUserNo(userNo, feedVisibleYn);
        inquiryRepository.updateVisibleYnByUserNo(userNo, inquiryVisibleYn);
    }

    /**
     * 소셜 계정 휴면 해제
     */
    @Transactional
    public void reactivateOAuth2User(Integer userNo) {
        final String userStatus = UserStatus.ACTIVE.getCode();
        final LocalDateTime userUpdated = LocalDateTime.now();

        UserEntity userEntity = UserEntity.builder()
                .userNo(userNo)
                .userStatus(userStatus)
                .userUpdated(userUpdated)
                .build();

        userRepository.reactivateUser(userEntity);

        final String feedVisibleYn = FeedVisibleStatus.VISIBLE.getCode();
        final String inquiryVisibleYn = InquiryVisibleCode.VISIBLE.getCode();

        feedRepository.updateFeedVisibleYnByUserNo(userNo, feedVisibleYn);
        inquiryRepository.updateVisibleYnByUserNo(userNo, inquiryVisibleYn);
    }

    /**
     * 탈퇴 소셜 계정 재가입 처리
     */
    @Transactional
    public void reactivateWithdrawnOAuth2User(Integer userNo, OAuth2UserResponse oAuth2UserResponse) {
        final String userNm = oAuth2UserResponse.getUserNm();
        final String userEmail = oAuth2UserResponse.getUserEmail();
        final String userNickname = oAuth2UserResponse.getUserNickname();
        final String userStatus = UserStatus.ACTIVE.getCode();
        final LocalDateTime userUpdated = LocalDateTime.now();

        if (userRepository.existUserEmail(userEmail)) {
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_DUPLICATED);
        }

        userRepository.reactivateUser(UserEntity.builder()
                .userNo(userNo)
                .userStatus(userStatus)
                .userUpdated(userUpdated)
                .build());

        userRepository.update(UserEntity.builder()
                .userNo(userNo)
                .userNm(userNm)
                .userEmail(userEmail)
                .userNickname(userNickname)
                .userUpdated(userUpdated)
                .build());
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void withdrawUser(Integer userNo, UserWithdrawRequest request) {
        final String userPassword = request.getUserPassword();
        final String withdrawnStatus = UserStatus.WITHDRAWN.getCode();
        final LocalDateTime userWithdrawn = LocalDateTime.now();

        UserEntity data = userRepository.selectUserByNo(userNo);
        if (data == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String userOauthProvider = data.getUserOauthProvider();
        if (userOauthProvider == null) {
            if (StringUtils.isBlank(userPassword)) {
                throw new ClimbStyleException(UserErrorCode.USER_PASSWORD_MISMATCH);
            }

            if (!passwordEncoder.matches(userPassword, data.getUserPassword())) {
                throw new ClimbStyleException(UserErrorCode.USER_PASSWORD_MISMATCH);
            }
        }

        UserEntity userEntity = UserEntity.builder()
                .userNo(userNo)
                .userStatus(withdrawnStatus)
                .userWithdrawn(userWithdrawn)
                .build();

        userRepository.withdrawUser(userEntity);

        List<String> feedFilePaths = feedFileRepository.selectFeedFilePathsByUserNo(userNo);
        feedRepository.deleteByUserNo(userNo);
        inquiryRepository.deleteByUserNo(userNo);

        for (String filePath : feedFilePaths) {
            fileService.fileDelete(filePath);
        }
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePassword(Integer userNo, UserPasswordUpdateRequest request) {
        final String userPassword = request.getUserPassword();
        final String newUserPassword = passwordEncoder.encode(request.getNewUserPassword());

        UserEntity data = userRepository.selectUserByNo(userNo);
        if (data == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String userOauthProvider = data.getUserOauthProvider();
        final String currentUserPassword = data.getUserPassword();

        if (userOauthProvider != null) {
            throw new ClimbStyleException(UserErrorCode.USER_OAUTH_NO_PASSWORD);
        }

        if (!passwordEncoder.matches(userPassword, currentUserPassword)) {
            throw new ClimbStyleException(UserErrorCode.USER_PASSWORD_MISMATCH);
        }

        final LocalDateTime userUpdated = LocalDateTime.now();

        UserEntity userEntity = UserEntity.builder()
                .userNo(userNo)
                .userPassword(newUserPassword)
                .userUpdated(userUpdated)
                .build();

        userRepository.updatePassword(userEntity);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public void updateUser(Integer userNo, UserUpdateRequest request) {
        final String userNm = request.getUserNm();
        final String userNickname = request.getUserNickname();
        final String userIntro = request.getUserIntro();
        final MultipartFile userProfileImg = request.getUserProfileImg();
        final String userProfileDelete = request.getUserProfileDelete();

        UserEntity data = userRepository.selectUserByNo(userNo);
        if (data == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String userStatus = data.getUserStatus();
        final String curentUserNickname = data.getUserNickname();
        final String userDeleteStatus = UserStatus.DORMANT.getCode();
        if (StringUtils.equals(userStatus, userDeleteStatus)) {
            throw new ClimbStyleException(UserErrorCode.USER_DORMANT_FORBIDDEN);
        }

        if (!StringUtils.equals(userNickname, curentUserNickname)) {
            Boolean existNickname = userRepository.existUserNickname(userNickname);
            if (existNickname) {
                throw new ClimbStyleException(UserErrorCode.USER_NICKNAME_DUPLICATED);
            }
        }

        String userImageUrl = data.getUserImageUrl();

        if (StringUtils.equals(DELETE_FLAG, userProfileDelete)) {
            if (userImageUrl != null) {
                fileService.fileDelete(userImageUrl);
                userImageUrl = null;
            }

        } else if (userProfileImg != null && !userProfileImg.isEmpty()) {
            String oldUserImageUrl = data.getUserImageUrl();
            String extension = FilenameUtils.getExtension(userProfileImg.getOriginalFilename());
            String storedFilename = String.format("%d_%s.%s", userNo, UUID.randomUUID().toString().replaceAll("-", ""), extension);

            userImageUrl = fileService.fileUpload(userProfileImg, FileTypeCode.USER_PROFILE, storedFilename);

            if (oldUserImageUrl != null) {
                fileService.fileDelete(oldUserImageUrl);
            }
        }

        final LocalDateTime userUpdated = LocalDateTime.now();

        UserEntity user = UserEntity.builder()
                .userNo(userNo)
                .userNm(userNm)
                .userNickname(userNickname)
                .userImageUrl(userImageUrl)
                .userIntro(userIntro)
                .userUpdated(userUpdated)
                .build();

        userRepository.update(user);
    }
}
