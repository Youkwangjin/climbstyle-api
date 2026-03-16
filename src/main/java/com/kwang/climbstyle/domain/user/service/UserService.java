package com.kwang.climbstyle.domain.user.service;

import com.kwang.climbstyle.code.feed.FeedVisibleStatus;
import com.kwang.climbstyle.code.file.FileTypeCode;
import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.code.role.RoleCode;
import com.kwang.climbstyle.code.user.UserStatus;
import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.domain.feed.repository.FeedRepository;
import com.kwang.climbstyle.domain.file.service.FileService;
import com.kwang.climbstyle.domain.role.entity.RoleEntity;
import com.kwang.climbstyle.domain.role.entity.UserRoleEntity;
import com.kwang.climbstyle.domain.role.repository.RoleRepository;
import com.kwang.climbstyle.domain.role.repository.UserRoleRepository;
import com.kwang.climbstyle.domain.user.dto.request.*;
import com.kwang.climbstyle.domain.user.dto.response.UserProfileResponse;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final FileService fileService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final FeedRepository feedRepository;

    private final PasswordEncoder passwordEncoder;

    public static final String DELETE_FLAG = "true";

    @Transactional(readOnly = true)
    public void checkUserIdDuplicate(UserIdRequest request) {
        final String userId = request.getUserId();
        Boolean existId = userRepository.existUserId(userId);

        if (existId) {
            throw new ClimbStyleException(UserErrorCode.USER_ID_DUPLICATED);
        }
    }

    @Transactional(readOnly = true)
    public void checkUserEmailDuplicate(UserEmailRequest request) {
        final String userEmail = request.getUserEmail();
        Boolean existEmail = userRepository.existUserEmail(userEmail);

        if (existEmail) {
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_DUPLICATED);
        }
    }

    @Transactional(readOnly = true)
    public void checkUserNicknameDuplicate(UserNicknameRequest request) {
        final String userNickName = request.getUserNickname();
        Boolean existNickname = userRepository.existUserNickname(userNickName);

        if (existNickname) {
            throw new ClimbStyleException(UserErrorCode.USER_NICKNAME_DUPLICATED);
        }
    }

    public UserProfileResponse selectUserByNo(Integer userNo) {
        UserEntity data = userRepository.selectUserByNo(userNo);
        if (data == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String userNm = data.getUserNm();
        final String userEmail = data.getUserEmail();
        final String userNickname = data.getUserNickname();
        final String userStatus = data.getUserStatus();
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
                .userImgUrl(userImageUrl)
                .userIntro(userIntro)
                .userCreated(userCreated)
                .userUpdated(userUpdated)
                .userDeactivated(userDeactivated)
                .build();
    }

    @Transactional
    public void createUser(UserCreateRequest request) {
        final String userId = request.getUserId();
        final String userPassword = passwordEncoder.encode(request.getUserPassword());
        final String userNm = request.getUserNm();
        final String userEmail = request.getUserEmail();
        final String userNickname = request.getUserNickname();
        final String userStatus = UserStatus.ACTIVE.getCode();
        final LocalDateTime userCreated = LocalDateTime.now();

        Boolean existId = userRepository.existUserId(userId);
        if (existId) {
            throw new ClimbStyleException(UserErrorCode.USER_ID_DUPLICATED);
        }

        Boolean existEmail = userRepository.existUserEmail(userEmail);
        if (existEmail) {
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_DUPLICATED);
        }

        Boolean existNickname = userRepository.existUserNickname(userNickname);
        if (existNickname) {
            throw new ClimbStyleException(UserErrorCode.USER_NICKNAME_DUPLICATED);
        }

        UserEntity user = UserEntity.builder()
                .userId(userId)
                .userPassword(userPassword)
                .userNm(userNm)
                .userEmail(userEmail)
                .userNickname(userNickname)
                .userStatus(userStatus)
                .userCreated(userCreated)
                .build();

        userRepository.insert(user);

        RoleEntity role = roleRepository.selectRoleByRoleName(RoleCode.ROLE_USER.getCode());
        if (role == null) {
            throw new ClimbStyleException(HttpErrorCode.INTERNAL_SERVER_ERROR);
        }

        final Integer roleNo = role.getRoleNo();
        final Integer userNo = user.getUserNo();

        UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                .userNo(userNo)
                .roleNo(roleNo)
                .build();

        userRoleRepository.insert(userRoleEntity);
    }

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
        feedRepository.updateFeedVisibleYnByUserNo(userNo, feedVisibleYn);
    }

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
        feedRepository.updateFeedVisibleYnByUserNo(userNo, feedVisibleYn);
    }

    @Transactional
    public void changePassword(Integer userNo, UserPasswordUpdateRequest request) {
        final String userPassword = request.getUserPassword();
        final String newUserPassword = passwordEncoder.encode(request.getNewUserPassword());

        UserEntity data = userRepository.selectUserByNo(userNo);
        if (data == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String currentUserPassword = data.getUserPassword();
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
            String storedFilename = String.format("%d_%s.%s", userNo, UUID.randomUUID().toString().replaceAll("-", ""),extension);

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
