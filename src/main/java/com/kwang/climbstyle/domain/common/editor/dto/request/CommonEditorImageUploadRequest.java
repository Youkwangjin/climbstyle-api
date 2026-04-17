package com.kwang.climbstyle.domain.common.editor.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class CommonEditorImageUploadRequest {

    @NotNull
    private MultipartFile file;
}
