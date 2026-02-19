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

    @NotBlank()
    @Size(min = 1, max = 50)
    private String userNm;

    @NotBlank()
    @Size(min = 2, max = 10)
    private String userNickName;

    private MultipartFile userProfileImg;

    private String userProfileDelete;

    @Size(max = 200)
    private String userIntro;
}
