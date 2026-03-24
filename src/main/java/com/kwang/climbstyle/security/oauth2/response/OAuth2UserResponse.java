package com.kwang.climbstyle.security.oauth2.response;

public interface OAuth2UserResponse {

    String getProvider();

    String getOAuthId();

    String getUserNm();

    String getUserEmail();

    String getUserNickname();

    String getNameAttributeKey();
}
