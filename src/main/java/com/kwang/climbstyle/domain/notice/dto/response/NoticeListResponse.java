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

    private Integer noticeCategory;

    private String noticeTitle;

    private Integer noticeHit;

    private Integer noticeFileCnt;

    private String noticePinYn;

    private LocalDateTime noticeCreated;
}
