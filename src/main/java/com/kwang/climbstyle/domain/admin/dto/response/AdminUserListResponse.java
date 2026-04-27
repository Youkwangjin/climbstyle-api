package com.kwang.climbstyle.domain.admin.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AdminUserListResponse {

    private Integer userNo;

    private String userId;

    private String userNm;

    private String userEmail;

    private String userNickname;

    private String userStatus;

    private String userImageUrl;

    private LocalDateTime userCreated;
}
