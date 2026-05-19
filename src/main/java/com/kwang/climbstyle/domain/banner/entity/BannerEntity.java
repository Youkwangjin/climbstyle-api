package com.kwang.climbstyle.domain.banner.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerEntity {

    private Integer bannerNo;

    private Integer adminNo;

    private String bannerImagePath;

    private Integer bannerOrder;

    private String bannerVisibleYn;

    private LocalDateTime bannerCreated;

    private LocalDateTime bannerUpdated;
}
