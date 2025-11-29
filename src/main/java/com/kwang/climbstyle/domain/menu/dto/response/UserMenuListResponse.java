package com.kwang.climbstyle.domain.menu.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserMenuListResponse {

    private String menuName;

    private String menuUrl;
}
