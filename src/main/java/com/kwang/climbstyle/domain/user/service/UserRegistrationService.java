package com.kwang.climbstyle.domain.user.service;

import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.code.role.RoleCode;
import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.code.user.UserStatus;
import com.kwang.climbstyle.domain.role.entity.RoleEntity;
import com.kwang.climbstyle.domain.role.entity.UserRoleEntity;
import com.kwang.climbstyle.domain.role.repository.RoleRepository;
import com.kwang.climbstyle.domain.role.repository.UserRoleRepository;
import com.kwang.climbstyle.domain.user.dto.request.UserCreateRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserIdRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserNicknameRequest;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import com.kwang.climbstyle.security.oauth2.response.OAuth2UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 사용자 회원가입 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 아이디 중복 확인
     */
    @Transactional(readOnly = true)
    public void checkUserIdDuplicate(UserIdRequest request) {
        final String userId = request.getUserId();
        Boolean existId = userRepository.existUserId(userId);

        if (existId) {
            throw new ClimbStyleException(UserErrorCode.USER_ID_DUPLICATED);
        }
    }

    /**
     * 닉네임 중복 확인
     */
    @Transactional(readOnly = true)
    public void checkUserNicknameDuplicate(UserNicknameRequest request) {
        final String userNickName = request.getUserNickname();
        Boolean existNickname = userRepository.existUserNickname(userNickName);

        if (existNickname) {
            throw new ClimbStyleException(UserErrorCode.USER_NICKNAME_DUPLICATED);
        }
    }

    /**
     * 일반 회원 등록
     */
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

    /**
     * 소셜 회원 등록
     */
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
}
