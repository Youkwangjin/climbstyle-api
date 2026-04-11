package com.kwang.climbstyle.domain.notice.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NoticeNavResponse {

    private Integer noticeNo;

    private String noticeTitle;

    private LocalDateTime noticeCreated;
}
