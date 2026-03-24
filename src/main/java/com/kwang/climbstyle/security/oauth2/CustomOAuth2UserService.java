package com.kwang.climbstyle.security.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final String PROVIDER_NAVER = "NAVER";

    private static final String PROVIDER_GOOGLE = "GOOGLE";

    private final NaverOAuth2UserService naverOAuth2UserService;

    private final GoogleOAuth2UserService googleOAuth2UserService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String provider = userRequest.getClientRegistration()
                                     .getRegistrationId()
                                     .toUpperCase();

        log.info("소셜 로그인 시도 - provider: {}", provider);

        if (StringUtils.equals(provider, PROVIDER_NAVER)) {
            return naverOAuth2UserService.loadUser(userRequest);
        }

        if (StringUtils.equals(provider, PROVIDER_GOOGLE)) {
            return googleOAuth2UserService.loadUser(userRequest);
        }

        log.warn("지원하지 않는 소셜 로그인 시도 - provider: {}", provider);
        throw new OAuth2AuthenticationException(new OAuth2Error("unsupported_provider"),
                                                                          "지원하지 않는 소셜 로그인입니다: " + provider
        );
    }
}
