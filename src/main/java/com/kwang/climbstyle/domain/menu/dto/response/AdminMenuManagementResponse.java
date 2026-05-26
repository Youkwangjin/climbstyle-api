package com.kwang.climbstyle.domain.menu.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AdminMenuManagementResponse {

    private Integer menuNo;

    private String menuCode;

    private String menuName;

    private String menuUrl;

    private Integer menuParentNo;

    private Integer menuLevel;

    private Integer menuSortOrder;

    private String menuUseYn;

    private String menuIcon;

    private LocalDateTime menuCreated;
}
