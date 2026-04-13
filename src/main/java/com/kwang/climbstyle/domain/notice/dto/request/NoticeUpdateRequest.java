package com.kwang.climbstyle.domain.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NoticeUpdateRequest {

    @NotBlank
    @Pattern(regexp = "^[012]$")
    private String noticeCategory;

    @NotBlank
    @Size(min = 1, max = 100)
    private String noticeTitle;

    private String noticeContent;

    @NotBlank
    @Pattern(regexp = "^[YN]$")
    private String noticePinYn;

    @NotBlank
    @Pattern(regexp = "^[YN]$")
    private String noticeVisibleYn;

    private List<MultipartFile> noticeFiles;

    private List<Integer> noticeFileIds;
}
