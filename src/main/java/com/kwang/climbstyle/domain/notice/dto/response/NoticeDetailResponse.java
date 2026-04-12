package com.kwang.climbstyle.domain.notice.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NoticeDetailResponse {

    private Integer noticeNo;

    private String noticeCategory;

    private String noticeTitle;

    private String noticeContent;

    private Integer noticeHit;

    private String noticePinYn;

    private String noticeVisibleYn;

    private LocalDateTime noticeCreated;

    private List<NoticeFileResponse> noticeFiles;

    private NoticeNavResponse prevNotice;

    private NoticeNavResponse nextNotice;
}
