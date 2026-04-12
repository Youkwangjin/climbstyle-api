package com.kwang.climbstyle.domain.notice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeEntity {

    private Integer noticeNo;

    private Integer adminNo;

    private String noticeCategory;

    private String noticeTitle;

    private String noticeContent;

    private String noticeContentText;

    private Integer noticeHit;

    private Integer noticeFileCnt;

    private String noticePinYn;

    private String noticeVisibleYn;

    private LocalDateTime noticeCreated;

    private LocalDateTime noticeUpdated;
}
