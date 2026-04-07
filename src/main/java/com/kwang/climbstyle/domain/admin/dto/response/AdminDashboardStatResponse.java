package com.kwang.climbstyle.domain.admin.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminDashboardStatResponse {

    private Integer totalUserCount;

    private Integer todayUserCount;

    private Integer totalFeedCount;

    private Integer todayFeedCount;

    private Integer pendingInquiryCount;

    private Integer totalNoticeCount;

    private Integer visibleNoticeCount;
}
