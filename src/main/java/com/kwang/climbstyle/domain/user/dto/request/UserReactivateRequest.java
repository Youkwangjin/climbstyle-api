package com.kwang.climbstyle.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserReactivateRequest {

    private String userId;

    private String userPassword;
}
