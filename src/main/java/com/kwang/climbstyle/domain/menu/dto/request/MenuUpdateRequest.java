package com.kwang.climbstyle.domain.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuUpdateRequest {

    @NotBlank
    @Size(max = 100)
    private String menuCode;

    @NotBlank
    @Size(max = 50)
    private String menuName;

    @Size(max = 255)
    private String menuUrl;

    private Integer menuParentNo;

    @NotNull
    private Integer menuLevel;

    @NotNull
    private Integer menuSortOrder;

    @Size(max = 100)
    private String menuIcon;

    @NotBlank
    @Pattern(regexp = "^[YN]$")
    private String menuUseYn;
}
