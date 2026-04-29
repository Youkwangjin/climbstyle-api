package com.kwang.climbstyle.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserProfileResponse {

    private Integer userNo;

    private String userNm;

    private String userNickname;

    private String userEmail;

    private String userStatus;

    private String userOauthProvider;

    private String userImgUrl;

    private String userIntro;

    private LocalDateTime userCreated;

    private LocalDateTime userUpdated;

    private LocalDateTime userDeactivated;
}
