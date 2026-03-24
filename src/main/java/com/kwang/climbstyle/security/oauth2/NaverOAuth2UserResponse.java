package com.kwang.climbstyle.security.oauth2;

import java.util.Map;

public class NaverOAuth2UserResponse {

    private final String provider;

    private final Map<String, Object> attributes;

    public NaverOAuth2UserResponse(String provider, Map<String, Object> attributes) {
        this.provider = provider;
        this.attributes = attributes;
    }

    public String getProvider() {
        return provider;
    }

    public String getOAuthId() {
        return (String) attributes.get("id");
    }

    public String getUserNm() {
        return (String) attributes.get("name");
    }

    public String getUserEmail() {
        return (String) attributes.get("email");
    }

    public String getUserNickname() {
        return (String) attributes.get("nickname");
    }
}
