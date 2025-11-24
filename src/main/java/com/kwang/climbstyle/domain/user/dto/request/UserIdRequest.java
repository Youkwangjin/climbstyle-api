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
public class UserIdRequest {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Size(min = 5, max = 20, message = "아이디는 5~20자 사이여야 합니다.")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "아이디는 영문과 숫자만 사용할 수 있습니다.")
    private String userId;
}
