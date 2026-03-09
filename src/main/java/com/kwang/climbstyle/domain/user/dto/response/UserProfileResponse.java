package com.kwang.climbstyle.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserProfileResponse {

    private Integer userNo;

    private String userNm;

    private String userNickName;

    private String userEmail;

    private String userDeleteYn;

    private String userImgUrl;

    private String userIntro;

    private LocalDateTime userCreated;

    private LocalDateTime userUpdated;

    private LocalDateTime userDeleted;
}
