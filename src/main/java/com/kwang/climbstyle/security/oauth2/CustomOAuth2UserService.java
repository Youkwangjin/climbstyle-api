package com.kwang.climbstyle.security.oauth2;

import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.code.role.RoleCode;
import com.kwang.climbstyle.code.user.UserStatus;
import com.kwang.climbstyle.domain.role.entity.RoleEntity;
import com.kwang.climbstyle.domain.role.entity.UserRoleEntity;
import com.kwang.climbstyle.domain.role.repository.RoleRepository;
import com.kwang.climbstyle.domain.role.repository.UserRoleRepository;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration()
                                     .getRegistrationId()
                                     .toUpperCase();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        OAuth2UserResponse oAuth2UserResponse = new OAuth2UserResponse(provider, response);

        Map<String, Object> customAttributes = new HashMap<>(response);
        customAttributes.put("oAuth2UserResponse", oAuth2UserResponse);

        UserEntity user = userRepository.selectUserByOAuthId(provider, oAuth2UserResponse.getOAuthId());
        if (user == null) {
            Boolean nicknameExists = userRepository.existUserNickname(oAuth2UserResponse.getUserNickname());
            if (nicknameExists) {
                customAttributes.put("needNicknameSetup", true);

                return new DefaultOAuth2User(
                        List.of(new SimpleGrantedAuthority(RoleCode.ROLE_TEMP_USER.getCode())),
                        customAttributes,
                        "id"
                );
            }

            final String userId = oAuth2UserResponse.getProvider().toLowerCase()
                                + "-"
                                + oAuth2UserResponse.getOAuthId().substring(0, 8);
            final String userNm = oAuth2UserResponse.getUserNm();
            final String userEmail = oAuth2UserResponse.getUserEmail();
            final String userNickname = oAuth2UserResponse.getUserNickname();
            final String userStatus = UserStatus.ACTIVE.getCode();
            final String userOauthProvider =  oAuth2UserResponse.getProvider();
            final String userOauthId =  oAuth2UserResponse.getOAuthId();
            final LocalDateTime userCreated =  LocalDateTime.now();

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
                throw new ClimbStyleException(HttpErrorCode.INTERNAL_SERVER_ERROR);
            }

            UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                    .userNo(user.getUserNo())
                    .roleNo(role.getRoleNo())
                    .build();

            userRoleRepository.insert(userRoleEntity);

            user = userRepository.selectUserByOAuthId(provider, oAuth2UserResponse.getOAuthId());
        }

        customAttributes.put("needNicknameSetup", false);
        customAttributes.put("userNo", user.getUserNo());

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(user.getUserRole())),
                customAttributes,
                "id"
        );
    }
}
