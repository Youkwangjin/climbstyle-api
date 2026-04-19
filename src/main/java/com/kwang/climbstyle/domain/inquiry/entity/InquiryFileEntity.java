package com.kwang.climbstyle.domain.inquiry.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryFileEntity {

    private Integer inquiryFileNo;

    private Integer inquiryNo;

    private String inquiryFileOriginalName;

    private String inquiryFileStoredName;

    private String inquiryFilePath;

    private String inquiryFileExtnsNm;

    private Integer inquiryFileSize;

    private LocalDateTime inquiryFileCreated;

    private LocalDateTime inquiryFileUpdated;
}
