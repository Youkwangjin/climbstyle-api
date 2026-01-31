package com.kwang.climbstyle.domain.feed.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class FeedCreateRequest {

    @NotBlank
    @Size(min = 1, max = 50)
    private String feedTitle;

    @Size(max = 500)
    private String feedContent;

    @NotBlank
    @Pattern(regexp = "^[YN]$")
    private String feedVisibleYn;

    @NotEmpty
    @Size(min = 1, max = 10)
    private List<MultipartFile> feedImages;
}
