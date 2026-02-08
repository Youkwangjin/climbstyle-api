package com.kwang.climbstyle.domain.feed.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FeedFileDetailResponse {

    private Integer feedFileNo;

    private Integer feedNo;

    private String feedFileOriginalName;

    private String feedFileStoredName;

    private String feedFilePath;

    private String feedFileExtnsNm;

    private String feedFileSize;

    private Integer feedFileSortOrder;

    private LocalDateTime feedFileCreated;

    private LocalDateTime feedFileUpdated;
}
