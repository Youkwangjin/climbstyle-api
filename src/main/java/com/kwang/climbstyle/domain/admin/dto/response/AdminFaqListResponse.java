package com.kwang.climbstyle.domain.admin.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AdminFaqListResponse {

    private Integer faqNo;

    private String faqQuestion;

    private String faqVisibleYn;

    private LocalDateTime faqCreated;
}
