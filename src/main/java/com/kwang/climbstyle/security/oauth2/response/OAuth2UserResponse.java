package com.kwang.climbstyle.security.oauth2.response;

/**
 * OAuth2 사용자 응답 인터페이스
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
public interface OAuth2UserResponse {

    String getProvider();

    String getOAuthId();

    String getUserNm();

    String getUserEmail();

    String getUserNickname();

    String getNameAttributeKey();
}
