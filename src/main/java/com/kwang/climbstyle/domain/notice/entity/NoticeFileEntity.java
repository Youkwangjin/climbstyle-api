package com.kwang.climbstyle.domain.notice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeFileEntity {

    private Integer noticeFileNo;

    private Integer noticeNo;

    private String noticeFileOriginalName;

    private String noticeFileStoredName;

    private String noticeFilePath;

    private String noticeFileExtnsNm;

    private String noticeFileSize;

    private LocalDateTime noticeFileCreated;

    private LocalDateTime noticeFileUpdated;
}
