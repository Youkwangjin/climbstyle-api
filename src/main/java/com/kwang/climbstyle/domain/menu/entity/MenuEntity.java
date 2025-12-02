package com.kwang.climbstyle.domain.menu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuEntity {

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

    private LocalDateTime menuUpdated;
}
