package com.kwang.climbstyle.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserNickNameRequest {

    @NotBlank
    @Size(min = 2, max = 10)
    private String userNickName;
}
