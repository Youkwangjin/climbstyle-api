package com.kwang.climbstyle.domain.menu.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdminMenuListResponse {

    private Integer menuNo;

    private String menuCode;

    private String menuName;

    private String menuUrl;

    private Integer menuParentNo;

    private Integer menuLevel;

    private Integer menuSortOrder;

    private String menuIcon;

    private List<AdminMenuListResponse> children = new ArrayList<>();
}
