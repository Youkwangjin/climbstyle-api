package com.kwang.climbstyle.domain.faq.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqEntity {

    private Integer faqNo;

    private Integer adminNo;

    private String faqQuestion;

    private String faqAnswer;

    private String faqVisibleYn;

    private LocalDateTime faqCreated;

    private LocalDateTime faqUpdated;
}
