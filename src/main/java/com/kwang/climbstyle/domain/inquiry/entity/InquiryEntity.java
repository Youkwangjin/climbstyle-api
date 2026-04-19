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
public class InquiryEntity {

    private Integer inquiryNo;

    private Integer userNo;

    private Integer adminNo;

    private String  inquiryTitle;

    private String  inquiryContent;

    private String  inquiryContentText;

    private String  inquiryStatus;

    private String  inquiryAnswer;

    private String inquiryDeleteYn;

    private LocalDateTime inquiryCreated;

    private LocalDateTime inquiryUpdated;
}
