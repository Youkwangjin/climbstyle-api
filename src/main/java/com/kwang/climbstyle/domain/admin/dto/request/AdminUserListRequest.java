package com.kwang.climbstyle.domain.admin.dto.request;

import com.kwang.climbstyle.common.protocal.CommonListRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminUserListRequest extends CommonListRequest {

    private String userStatus;
}
