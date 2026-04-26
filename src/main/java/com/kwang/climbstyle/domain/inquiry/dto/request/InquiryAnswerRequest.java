package com.kwang.climbstyle.domain.inquiry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InquiryAnswerRequest {

    @NotBlank
    private String inquiryAnswerContent;
}
