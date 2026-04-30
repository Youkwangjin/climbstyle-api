package com.kwang.climbstyle.security.oauth2.service;

import com.kwang.climbstyle.code.role.RoleCode;
import com.kwang.climbstyle.code.role.RoleErrorCode;
import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.code.user.UserStatus;
import com.kwang.climbstyle.domain.role.entity.RoleEntity;
import com.kwang.climbstyle.domain.role.entity.UserRoleEntity;
import com.kwang.climbstyle.domain.role.repository.RoleRepository;
import com.kwang.climbstyle.domain.role.repository.UserRoleRepository;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import com.kwang.climbstyle.domain.user.service.UserService;
import com.kwang.climbstyle.exception.ClimbStyleException;
import com.kwang.climbstyle.security.oauth2.response.GoogleOAuth2UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final UserService userService;

    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration()
                                     .getRegistrationId()
                                     .toUpperCase();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        GoogleOAuth2UserResponse googleOAuth2UserResponse = new GoogleOAuth2UserResponse(provider, attributes);

        Map<String, Object> customAttributes = new HashMap<>(attributes);
        customAttributes.put("oAuth2UserResponse", googleOAuth2UserResponse);

        UserEntity user = userRepository.selectUserByOAuthId(googleOAuth2UserResponse.getProvider(),
                                                             googleOAuth2UserResponse.getOAuthId());

        if (user == null) {
            Boolean emailExists = userRepository.existUserEmail(googleOAuth2UserResponse.getUserEmail());
            if (emailExists) {
                log.warn("소셜 로그인 이메일 중복 - provider: {}", googleOAuth2UserResponse.getProvider());

                throw new OAuth2AuthenticationException(
                        new OAuth2Error(UserErrorCode.USER_EMAIL_DUPLICATED.getCode()),
                        new ClimbStyleException(UserErrorCode.USER_EMAIL_DUPLICATED)
                );
            }

            Boolean nicknameExists = userRepository.existUserNickname(googleOAuth2UserResponse.getUserNickname());
            if (nicknameExists) {
                log.info("소셜 로그인 닉네임 중복 - provider: {}", googleOAuth2UserResponse.getProvider());

                customAttributes.put("needNicknameSetup", true);

                return new DefaultOAuth2User(
                        List.of(new SimpleGrantedAuthority(RoleCode.ROLE_TEMP_USER.getCode())),
                        customAttributes,
                        "sub"
                );
            }

            final String userId = googleOAuth2UserResponse.getProvider().toLowerCase()
                                + "-"
                                + googleOAuth2UserResponse.getOAuthId().substring(0, 8);
            final String userNm = googleOAuth2UserResponse.getUserNm();
            final String userEmail = googleOAuth2UserResponse.getUserEmail();
            final String userNickname = googleOAuth2UserResponse.getUserNickname();
            final String userStatus = UserStatus.ACTIVE.getCode();
            final String userOauthProvider = googleOAuth2UserResponse.getProvider();
            final String userOauthId = googleOAuth2UserResponse.getOAuthId();
            final LocalDateTime userCreated = LocalDateTime.now();

            user = UserEntity.builder()
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
                throw new ClimbStyleException(RoleErrorCode.ROLE_NOT_FOUND);
            }

            UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                    .userNo(user.getUserNo())
                    .roleNo(role.getRoleNo())
                    .build();

            userRoleRepository.insert(userRoleEntity);

            log.info("소셜 로그인 신규 회원 가입 - provider: {}", googleOAuth2UserResponse.getProvider());

            user = userRepository.selectUserByOAuthId(
                    googleOAuth2UserResponse.getProvider(),
                    googleOAuth2UserResponse.getOAuthId()
            );
        }

        if (StringUtils.equals(user.getUserStatus(), UserStatus.WITHDRAWN.getCode())) {
            log.info("소셜 로그인 탈퇴 계정 재가입 - userNo: {}", user.getUserNo());

            userService.reactivateWithdrawnOAuth2User(user.getUserNo(), googleOAuth2UserResponse);
            user = userRepository.selectUserByOAuthId(
                    googleOAuth2UserResponse.getProvider(),
                    googleOAuth2UserResponse.getOAuthId()
            );
        }

        if (StringUtils.equals(user.getUserStatus(), UserStatus.DORMANT.getCode())) {
            log.info("소셜 로그인 휴면 계정 자동 활성화 - userNo: {}", user.getUserNo());

            userService.reactivateOAuth2User(user.getUserNo());
            user = userRepository.selectUserByOAuthId(
                    googleOAuth2UserResponse.getProvider(),
                    googleOAuth2UserResponse.getOAuthId()
            );
        }

        if (StringUtils.equals(user.getUserStatus(), UserStatus.SUSPENDED.getCode())) {
            log.warn("소셜 로그인 정지 계정 접근 시도 - userNo: {}", user.getUserNo());

            throw new OAuth2AuthenticationException(
                    new OAuth2Error(UserErrorCode.USER_ALREADY_SUSPENDED.getCode()),
                    new ClimbStyleException(UserErrorCode.USER_ALREADY_SUSPENDED)
            );
        }

        if (user.getUserRole() == null) {
            log.error("소셜 로그인 권한 없음 - userNo: {}", user.getUserNo());

            throw new OAuth2AuthenticationException(
                    new OAuth2Error(RoleErrorCode.ROLE_NOT_FOUND.getCode()),
                    new ClimbStyleException(RoleErrorCode.ROLE_NOT_FOUND)
            );
        }

        customAttributes.put("needNicknameSetup", false);
        customAttributes.put("userNo", user.getUserNo());

        log.info("소셜 로그인 성공 - provider: {}, userNo: {}", googleOAuth2UserResponse.getProvider(), user.getUserNo());

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(user.getUserRole())),
                customAttributes,
                googleOAuth2UserResponse.getNameAttributeKey()
        );
    }
}
