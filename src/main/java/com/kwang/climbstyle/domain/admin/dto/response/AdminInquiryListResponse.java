package com.kwang.climbstyle.domain.admin.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AdminInquiryListResponse {

    private Integer inquiryNo;

    private Integer userNo;

    private String  inquiryTitle;

    private String  inquiryStatus;

    private LocalDateTime inquiryCreated;
}
