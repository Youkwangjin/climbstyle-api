package com.kwang.climbstyle.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminLoginRequest {

    @NotBlank
    @Size(min = 5, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String adminId;

    @NotBlank
    @Size(min = 4)
    private String adminPassword;
}
