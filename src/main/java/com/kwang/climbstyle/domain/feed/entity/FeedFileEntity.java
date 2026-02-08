package com.kwang.climbstyle.domain.feed.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedFileEntity {

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
