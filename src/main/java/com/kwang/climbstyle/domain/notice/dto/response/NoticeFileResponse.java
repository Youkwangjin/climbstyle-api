package com.kwang.climbstyle.domain.notice.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeFileResponse {

    private Integer noticeFileNo;

    private Integer noticeNo;

    private String noticeFileOriginalName;

    private String noticeFileStoredName;

    private String noticeFilePath;

    private String noticeFileExtnsNm;

    private String noticeFileSize;
}
