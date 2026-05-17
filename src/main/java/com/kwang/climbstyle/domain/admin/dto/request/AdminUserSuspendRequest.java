package com.kwang.climbstyle.domain.admin.dto.request;

import com.kwang.climbstyle.code.user.UserSuspendCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminUserSuspendRequest {

    private UserSuspendCategory suspendCategory;

    private String suspendReason;

    private Integer suspendDays;
}
