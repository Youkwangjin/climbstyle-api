package com.kwang.climbstyle.domain.inquiry.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InquiryDetailResponse {

    private Integer inquiryNo;

    private Integer userNo;

    private Integer adminNo;

    private String inquiryTitle;

    private String inquiryContent;

    private String inquiryStatus;

    private String inquiryAnswerContent;

    private String inquiryAnswerContentText;

    private String inquiryDeleteYn;

    private LocalDateTime inquiryCreated;

    private LocalDateTime inquiryUpdated;

    private List<InquiryFileResponse> inquiryFiles;
}
