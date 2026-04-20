package com.kwang.climbstyle.domain.inquiry.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InquiryUpdateRequest {

    @NotBlank
    @Size(min = 1, max = 50)
    private String inquiryTitle;

    @NotBlank
    @Size(max = 2000)
    private String inquiryContent;

    private List<MultipartFile> inquiryFiles;

    private List<Integer> inquiryFileIds;
}
