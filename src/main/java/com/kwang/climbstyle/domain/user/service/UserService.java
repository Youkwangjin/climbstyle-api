package com.kwang.climbstyle.domain.user.service;

import com.kwang.climbstyle.code.feed.FeedVisibleStatus;
import com.kwang.climbstyle.code.inquiry.InquiryVisibleCode;
import com.kwang.climbstyle.code.file.FileTypeCode;
import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.code.role.RoleCode;
import com.kwang.climbstyle.code.user.UserStatus;
import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.domain.admin.dto.request.AdminUserListRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminUserListResponse;
import com.kwang.climbstyle.domain.feed.repository.FeedFileRepository;
import com.kwang.climbstyle.domain.feed.repository.FeedRepository;
import com.kwang.climbstyle.domain.inquiry.repository.InquiryRepository;
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
import com.kwang.climbstyle.security.oauth2.response.OAuth2UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final FileService fileService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final FeedRepository feedRepository;

    private final FeedFileRepository feedFileRepository;

    private final InquiryRepository inquiryRepository;

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
    public void checkUserNicknameDuplicate(UserNicknameRequest request) {
        final String userNickName = request.getUserNickname();
        Boolean existNickname = userRepository.existUserNickname(userNickName);

        if (existNickname) {
            throw new ClimbStyleException(UserErrorCode.USER_NICKNAME_DUPLICATED);
        }
    }

    @Transactional(readOnly = true)
    public List<AdminUserListResponse> getAdminUserList(AdminUserListRequest request) {
        request.setTotalCount(userRepository.selectAdminUserListCountByRequest(request));

        return userRepository.selectAdminUserList(request);
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
    public void createOAuth2User(UserNicknameRequest request, HttpServletRequest httpServletRequest,
                                 OAuth2User oAuth2User) {

        OAuth2UserResponse oAuth2UserResponse = oAuth2User.getAttribute("oAuth2UserResponse");
        if (oAuth2UserResponse == null) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }

        final String userNickname = request.getUserNickname();
        Boolean existNickname = userRepository.existUserNickname(userNickname);
        if (existNickname) {
            throw new ClimbStyleException(UserErrorCode.USER_NICKNAME_DUPLICATED);
        }

        final String userId = oAuth2UserResponse.getProvider().toLowerCase()
                + "-"
                + oAuth2UserResponse.getOAuthId().substring(0, 8);
        final String userNm = oAuth2UserResponse.getUserNm();
        final String userEmail = oAuth2UserResponse.getUserEmail();
        final String userStatus = UserStatus.ACTIVE.getCode();
        final String userOauthProvider = oAuth2UserResponse.getProvider();
        final String userOauthId = oAuth2UserResponse.getOAuthId();
        final LocalDateTime userCreated = LocalDateTime.now();

        UserEntity user = UserEntity.builder()
                .userId(userId)
                .userNm(userNm)
                .userEmail(userEmail)
                .userNickname(userNickname)
                .userStatus(userStatus)
                .userOauthProvider(userOauthProvider)
                .userOauthId(userOauthId)
                .userCreated(userCreated)
                .build();

        userRepository.insert(user);

        RoleEntity role = roleRepository.selectRoleByRoleName(RoleCode.ROLE_USER.getCode());
        if (role == null) {
            throw new ClimbStyleException(HttpErrorCode.INTERNAL_SERVER_ERROR);
        }

        UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                .userNo(user.getUserNo())
                .roleNo(role.getRoleNo())
                .build();

        userRoleRepository.insert(userRoleEntity);

        UserEntity savedUser = userRepository.selectUserByOAuthId(
                oAuth2UserResponse.getProvider(),
                oAuth2UserResponse.getOAuthId()
        );

        Map<String, Object> updatedAttributes = new HashMap<>(oAuth2User.getAttributes());
        updatedAttributes.put("needNicknameSetup", false);
        updatedAttributes.put("userNo", savedUser.getUserNo());

        OAuth2User updatedOAuth2User = new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(savedUser.getUserRole())),
                updatedAttributes,
                oAuth2UserResponse.getNameAttributeKey()
        );

        OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(
                updatedOAuth2User,
                List.of(new SimpleGrantedAuthority(savedUser.getUserRole())),
                oAuth2UserResponse.getProvider().toLowerCase()
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        httpServletRequest.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );
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
        final String inquiryVisibleYn = InquiryVisibleCode.HIDDEN.getCode();

        feedRepository.updateFeedVisibleYnByUserNo(userNo, feedVisibleYn);
        inquiryRepository.updateVisibleYnByUserNo(userNo, inquiryVisibleYn);
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
        final String inquiryVisibleYn = InquiryVisibleCode.VISIBLE.getCode();

        feedRepository.updateFeedVisibleYnByUserNo(userNo, feedVisibleYn);
        inquiryRepository.updateVisibleYnByUserNo(userNo, inquiryVisibleYn);
    }

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

    @Transactional
    public void changePassword(Integer userNo, UserPasswordUpdateRequest request) {
        final String userPassword = request.getUserPassword();
        final String newUserPassword = passwordEncoder.encode(request.getNewUserPassword());

        UserEntity data = userRepository.selectUserByNo(userNo);
        if (data == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String userOauthProvider =  data.getUserOauthProvider();
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
