package com.kwang.climbstyle.domain.banner.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BannerUpdateRequest {

    @NotNull
    @Min(1)
    @Max(10)
    private Integer bannerOrder;

    @NotBlank
    @Pattern(regexp = "^[YN]$")
    private String bannerVisibleYn;
}
