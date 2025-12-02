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

    private String userEmail;

    private String userNickName;

    private String userDeleteYn;

    private String userImageUrl;

    private LocalDateTime userCreated;

    private LocalDateTime userUpdated;

    private LocalDateTime userDeleted;
}
