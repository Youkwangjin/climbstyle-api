package com.kwang.climbstyle.domain.banner.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BannerDetailResponse {

    private Integer bannerNo;

    private String bannerImagePath;

    private Integer bannerOrder;

    private String bannerVisibleYn;

    private LocalDateTime bannerCreated;

    private LocalDateTime bannerUpdated;
}
