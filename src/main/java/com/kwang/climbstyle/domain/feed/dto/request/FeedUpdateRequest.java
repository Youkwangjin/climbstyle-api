package com.kwang.climbstyle.domain.feed.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedUpdateRequest {

    @NotBlank
    @Size(min = 1, max = 50)
    private String feedTitle;

    @Size(max = 500)
    private String feedContent;

    @NotBlank
    @Pattern(regexp = "^[YN]$")
    private String feedVisibleYn;
}
