package com.kwang.climbstyle.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserNicknameRequest {

    @NotBlank
    @Size(min = 2, max = 20)
    @Pattern(regexp = "^(?!\\.)[가-힣a-zA-Z0-9_.]{2,20}(?<!\\.)$")
    private String userNickname;
}
