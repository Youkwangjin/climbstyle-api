package com.kwang.climbstyle.domain.admin.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AdminNoticeListResponse {

    private Integer noticeNo;

    private String noticeCategory;

    private String noticeTitle;

    private String noticeContent;

    private String noticePinYn;

    private String noticeVisibleYn;

    private LocalDateTime noticeCreated;
}
