package com.kwang.climbstyle.domain.inquiry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InquiryStatusRequest {

    @NotBlank
    private String inquiryStatus;
}
