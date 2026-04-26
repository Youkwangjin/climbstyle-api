package com.kwang.climbstyle.domain.faq.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FaqCreateRequest {

    @NotBlank
    @Size(min = 1, max = 200)
    private String faqQuestion;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String faqAnswer;

    @NotBlank
    @Pattern(regexp = "^[YN]$")
    private String faqVisibleYn;
}
