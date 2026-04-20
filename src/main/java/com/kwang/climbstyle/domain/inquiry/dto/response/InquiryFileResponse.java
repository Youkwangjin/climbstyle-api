package com.kwang.climbstyle.domain.inquiry.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InquiryFileResponse {

    private Integer inquiryFileNo;

    private Integer inquiryNo;

    private String inquiryFileOriginalName;

    private String inquiryFileStoredName;

    private String inquiryFilePath;

    private String inquiryFileExtnsNm;

    private String inquiryFileSize;
}
