package com.kwang.climbstyle.domain.faq.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FaqListResponse {

    private Integer faqNo;

    private String faqQuestion;

    private String faqAnswer;

    private LocalDateTime faqCreated;
}
