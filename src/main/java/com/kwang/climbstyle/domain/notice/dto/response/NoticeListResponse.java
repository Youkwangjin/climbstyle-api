package com.kwang.climbstyle.domain.notice.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NoticeListResponse {

    private Integer noticeNo;

    private String noticeCategory;

    private String noticeTitle;

    private Integer noticeHit;

    private String noticePinYn;

    private LocalDateTime noticeCreated;

    private Boolean noticeNew;

    private Boolean hasFile;
}
