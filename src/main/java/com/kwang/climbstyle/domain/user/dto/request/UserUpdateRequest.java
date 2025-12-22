package com.kwang.climbstyle.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(min = 1, max = 50, message = "비밀번호는 1~50자 사이여야 합니다.")
    private String userNm;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2~10자 사이여야 합니다.")
    private String userNickName;

    private MultipartFile userProfileImg;

    @Size(max = 200, message = "소개글은 200자 이내로 입력해 주세요.")
    private String userIntro;
}
