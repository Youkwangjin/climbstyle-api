package com.kwang.climbstyle.security.oauth2.response;

import java.util.Map;

/**
 * Naver OAuth2 사용자 응답 정보
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
public class NaverOAuth2UserResponse implements OAuth2UserResponse {

    private final String provider;

    private final Map<String, Object> attributes;

    public NaverOAuth2UserResponse(String provider, Map<String, Object> attributes) {
        this.provider = provider;
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public String getOAuthId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getUserNm() {
        return (String) attributes.get("name");
    }

    @Override
    public String getUserEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getUserNickname() {
        return (String) attributes.get("nickname");
    }

    @Override
    public String getNameAttributeKey() {
        return "id";
    }
}
