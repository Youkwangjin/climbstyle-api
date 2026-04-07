package com.kwang.climbstyle.domain.admin.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AdminUserListResponse {

    private String userEmail;

    private String userImageUrl;

    private String userNickname;

    private LocalDateTime userCreated;
}
