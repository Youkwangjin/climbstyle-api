package com.kwang.climbstyle.domain.inquiry.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InquiryListResponse {

    private Integer inquiryNo;

    private String inquiryTitle;

    private String inquiryStatus;

    private LocalDateTime inquiryCreated;
}
