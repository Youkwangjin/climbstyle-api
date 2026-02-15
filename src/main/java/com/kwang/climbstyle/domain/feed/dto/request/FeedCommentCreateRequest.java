package com.kwang.climbstyle.domain.feed.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedCommentCreateRequest {

    @NotBlank
    @Size(max = 500)
    private String feedCommentContent;

    private Integer feedCommentParentNo;
}
