package com.kwang.climbstyle.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    private Integer userNo;

    private String userId;

    private String userPassword;

    private String userNm;

    private String userEmail;

    private String userNickname;

    private String userStatus;

    private String userOauthProvider;

    private String userOauthId;

    private String userImageUrl;

    private String userIntro;

    private String userSuspendReason;

    private String userSuspendCategory;

    private String userRole;

    private LocalDateTime userCreated;

    private LocalDateTime userUpdated;

    private LocalDateTime userDeactivated;

    private LocalDateTime userWithdrawn;

    private LocalDateTime userSuspended;

    private LocalDateTime userSuspendUntil;
}
